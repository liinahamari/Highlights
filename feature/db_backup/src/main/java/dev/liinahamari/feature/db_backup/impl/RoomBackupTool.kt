@file:Suppress("UnsafeOptInUsageError")

package dev.liinahamari.feature.db_backup.impl

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment
import androidx.room.RoomDatabase
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV
import androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
import androidx.security.crypto.MasterKey
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

//todo make available from activity

/**
 * Cause [RoomBackupTool] tied to host fragment's lifecycle, programmer client are responsible to handle potential
 * memory leak. General approach is to instantiate class while [Fragment.onCreate] and make null while [Fragment.onDetach]
 * */


class RoomBackupTool(private val context: Context) {
    companion object {
        private lateinit var INTERNAL_BACKUP_PATH: File
        private lateinit var TEMP_BACKUP_PATH: File
        private lateinit var TEMP_BACKUP_FILE: File
        private lateinit var EXTERNAL_BACKUP_PATH: File
        private lateinit var DATABASE_FILE: File
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbName: String

    var roomDatabase: RoomDatabase? = null
    var appId: String? = null
    var enableLogDebug: Boolean = true
    var onCompleteListener: OnCompleteListener? = object : OnCompleteListener {
        override fun onComplete(success: Boolean, exitCode: Int) {
            Toast.makeText(context, "success: $success, exitCode: $exitCode", LENGTH_LONG)
                .show()
//            if (success) restartApp(Intent(this@MainActivity, MainActivity::class.java))
        }
    }

    fun initRoomBackup() {
        val masterKeyAlias = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            context.packageName,
            masterKeyAlias,
            AES256_SIV,
            AES256_GCM
        )

        dbName = roomDatabase!!.openHelper.databaseName!!
        INTERNAL_BACKUP_PATH = File("${context.filesDir}/databasebackup/")
        TEMP_BACKUP_PATH = File("${context.filesDir}/databasebackup-temp/")
        TEMP_BACKUP_FILE = File("$TEMP_BACKUP_PATH/tempbackup.sqlite3")
        EXTERNAL_BACKUP_PATH = File(context.getExternalFilesDir("backup")!!.toURI())
        DATABASE_FILE = File(context.getDatabasePath(dbName).toURI())

        INTERNAL_BACKUP_PATH.mkdirs()
        TEMP_BACKUP_PATH.mkdirs()

        if (enableLogDebug) {
            Log.d(javaClass.name, "DatabaseName: $dbName")
            Log.d(javaClass.name, "Database Location: $DATABASE_FILE")
            Log.d(javaClass.name, "INTERNAL_BACKUP_PATH: $INTERNAL_BACKUP_PATH")
            Log.d(javaClass.name, "EXTERNAL_BACKUP_PATH: $EXTERNAL_BACKUP_PATH")
        }
    }

    fun setDatabase(db: RoomDatabase) {
        roomDatabase = db
    }

    fun setApplicationId(applicationId: String) {
        this.appId = applicationId
    }

    fun getDatabaseName(): String = "$dbName-${getCurrentDayTimeStamp()}.sqlite3"

    private fun getCurrentDayTimeStamp(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)

    fun doBackup(destination: OutputStream) {
        initRoomBackup()
        roomDatabase!!.close()
        roomDatabase = null
        DATABASE_FILE.inputStream().copyTo(destination)
        if (enableLogDebug) Log.d(javaClass.name, "Backup done, and saved to $destination")
        onCompleteListener?.onComplete(true, OnCompleteListener.EXIT_CODE_SUCCESS)
    }

    fun doRestore(source: InputStream) {
        initRoomBackup()
        roomDatabase!!.close()
        roomDatabase = null

        source.use { input -> DATABASE_FILE.outputStream().use { output -> input.copyTo(output) } }
        if (enableLogDebug) Log.d(javaClass.name, "Restore done, and restored from $source")
        onCompleteListener?.onComplete(true, OnCompleteListener.EXIT_CODE_SUCCESS)
    }
}
