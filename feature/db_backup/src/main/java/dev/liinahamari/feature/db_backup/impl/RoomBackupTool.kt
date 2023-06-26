package dev.liinahamari.feature.db_backup.impl

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.core.os.BuildCompat.PrereleaseSdkCheck
import androidx.core.os.BuildCompat.isAtLeastT
import androidx.fragment.app.Fragment
import androidx.room.RoomDatabase
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV
import androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
import androidx.security.crypto.MasterKey
import com.google.common.io.Files.copy
import dev.liinahamari.feature.db_backup.impl.OnCompleteListener.Companion.EXIT_CODE_ERROR_BACKUP_FILE_CHOOSER
import dev.liinahamari.feature.db_backup.impl.OnCompleteListener.Companion.EXIT_CODE_ERROR_BACKUP_FILE_CREATOR
import dev.liinahamari.feature.db_backup.impl.OnCompleteListener.Companion.EXIT_CODE_ERROR_STORAGE_PERMISSONS_NOT_GRANTED
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RoomBackupTool(private val fragment: Fragment) {
    companion object {
        private lateinit var INTERNAL_BACKUP_PATH: File
        private lateinit var TEMP_BACKUP_PATH: File
        private lateinit var TEMP_BACKUP_FILE: File
        private lateinit var EXTERNAL_BACKUP_PATH: File
        private lateinit var DATABASE_FILE: File

        private var currentProcess: Int? = null
        private const val PROCESS_BACKUP = 1
        private const val PROCESS_RESTORE = 2
        private var backupFilename: String? = null
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbName: String

    var roomDatabase: RoomDatabase? = null
    var enableLogDebug: Boolean = true
    var onCompleteListener: OnCompleteListener? = object : OnCompleteListener {
        override fun onComplete(success: Boolean, message: String, exitCode: Int) {
            Toast.makeText(
                fragment.requireContext(),
                "success: $success, message: $message, exitCode: $exitCode",
                Toast.LENGTH_LONG
            ).show()
//            if (success) restartApp(Intent(this@MainActivity, MainActivity::class.java))
        }
    }

    private fun initRoomBackup(): Boolean {
        val masterKeyAlias = MasterKey.Builder(fragment.requireContext())
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            fragment.requireContext(),
            fragment.requireContext().packageName,
            masterKeyAlias,
            AES256_SIV,
            AES256_GCM
        )

        dbName = roomDatabase!!.openHelper.databaseName!!
        INTERNAL_BACKUP_PATH = File("${fragment.requireContext().filesDir}/databasebackup/")
        TEMP_BACKUP_PATH = File("${fragment.requireContext().filesDir}/databasebackup-temp/")
        TEMP_BACKUP_FILE = File("$TEMP_BACKUP_PATH/tempbackup.sqlite3")
        EXTERNAL_BACKUP_PATH = File(fragment.requireContext().getExternalFilesDir("backup")!!.toURI())
        DATABASE_FILE = File(fragment.requireContext().getDatabasePath(dbName).toURI())

        INTERNAL_BACKUP_PATH.mkdirs()
        TEMP_BACKUP_PATH.mkdirs()

        if (enableLogDebug) {
            Log.d(javaClass.name, "DatabaseName: $dbName")
            Log.d(javaClass.name, "Database Location: $DATABASE_FILE")
            Log.d(javaClass.name, "INTERNAL_BACKUP_PATH: $INTERNAL_BACKUP_PATH")
            Log.d(javaClass.name, "EXTERNAL_BACKUP_PATH: $EXTERNAL_BACKUP_PATH")
        }
        return true
    }

    fun setDatabase(db: RoomDatabase) {
        roomDatabase = db
    }

    @PrereleaseSdkCheck
    fun backup() {
        val success = initRoomBackup()
        if (!success) return

        currentProcess = PROCESS_BACKUP

        val filename =
            "$dbName-${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)}.sqlite3"
        if (enableLogDebug) Log.d(javaClass.name, "backupFilename: $filename")

        backupFilename = filename
        permissionRequestLauncher.launch(
            if (isAtLeastT()) arrayOf(
                READ_MEDIA_IMAGES,
                READ_MEDIA_AUDIO,
                READ_MEDIA_VIDEO
            ) else arrayOf(READ_EXTERNAL_STORAGE)
        )
    }

    private fun doBackup(destination: OutputStream) {
        roomDatabase!!.close()
        roomDatabase = null
        copy(DATABASE_FILE, destination)
        if (enableLogDebug) Log.d(javaClass.name, "Backup done, and saved to $destination")
        onCompleteListener?.onComplete(true, "success", OnCompleteListener.EXIT_CODE_SUCCESS)
    }

    @PrereleaseSdkCheck
    fun restore() {
        if (initRoomBackup().not()) return

        currentProcess = PROCESS_RESTORE
        permissionRequestLauncher.launch(
            if (isAtLeastT()) arrayOf(
                READ_MEDIA_IMAGES,
                READ_MEDIA_AUDIO,
                READ_MEDIA_VIDEO
            ) else arrayOf(READ_EXTERNAL_STORAGE)
        )
    }

    private fun doRestore(source: InputStream) {
        roomDatabase!!.close()
        roomDatabase = null

        source.use { input -> DATABASE_FILE.outputStream().use { output -> input.copyTo(output) } }
        if (enableLogDebug) Log.d(javaClass.name, "Restore done, and restored from $source")
        onCompleteListener?.onComplete(true, "success", OnCompleteListener.EXIT_CODE_SUCCESS)
    }

    private val permissionRequestLauncher =
        fragment.registerForActivityResult(RequestMultiplePermissions()) { permissions ->
            if (permissions.entries.map { it.value }.all { it }.not()) {
                onCompleteListener?.onComplete(
                    false,
                    "storage permissions are required, please allow!",
                    EXIT_CODE_ERROR_STORAGE_PERMISSONS_NOT_GRANTED
                )
                return@registerForActivityResult
            }

            when (currentProcess) {
                PROCESS_BACKUP -> openBackupFileCreator.launch(backupFilename)
                PROCESS_RESTORE -> openBackupFileChooser.launch(arrayOf("application/octet-stream"))
            }
        }

    private val openBackupFileChooser = fragment.registerForActivityResult(OpenDocument()) { result ->
        if (result != null) {
            fragment.requireActivity().contentResolver.openInputStream(result)?.let(::doRestore)
            return@registerForActivityResult
        }
        onCompleteListener?.onComplete(false, "failure", EXIT_CODE_ERROR_BACKUP_FILE_CHOOSER)
    }

    private val openBackupFileCreator =
        fragment.registerForActivityResult(CreateDocument("application/octet-stream")) { result ->
            if (result != null) {
                fragment.requireActivity().contentResolver.openOutputStream(result)?.let(::doBackup)
                return@registerForActivityResult
            }
            onCompleteListener?.onComplete(false, "failure", EXIT_CODE_ERROR_BACKUP_FILE_CREATOR)
        }
}
