package dev.liinahamari.feature.db_backup.impl

import android.content.SharedPreferences
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

private const val BACKUP_SECRET_KEY = "shared_pref_backup_secret_key"

class SecretKeyManager(private val sharedPrefs: SharedPreferences) {
    private fun getPasswordFrom(sharedPrefs: SharedPreferences): String =
        sharedPrefs.getString(BACKUP_SECRET_KEY, null) ?: generatePassword().also {
            sharedPrefs.edit().putString(
                BACKUP_SECRET_KEY, it
            ).apply()
        }

    private fun generatePassword(): String {
        val stringLength = 20
        val charset = ('a'..'z') + ('A'..'Z') + ('1'..'9') + '!' + '@' + '#' + '%' + '^' + '&' + '*'
        return (1..stringLength).map { charset.random() }.joinToString("")
    }

    fun getSecretKey(salt: ByteArray): SecretKey = generateSecretKey(getPasswordFrom(sharedPrefs).toCharArray(), salt)

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    private fun generateSecretKey(password: CharArray, salt: ByteArray): SecretKey = SecretKeySpec(
        SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            .generateSecret(PBEKeySpec(password, salt, 65536, 128)).encoded, "AES"
    )
}
