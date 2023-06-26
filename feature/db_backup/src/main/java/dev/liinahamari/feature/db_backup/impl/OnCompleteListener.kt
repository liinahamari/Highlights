package dev.liinahamari.feature.db_backup.impl

interface OnCompleteListener {
    fun onComplete(success: Boolean, exitCode: Int)

    companion object {
        const val EXIT_CODE_SUCCESS = 1
        const val EXIT_CODE_ERROR = 0
        const val EXIT_CODE_ERROR_BACKUP_FILE_CHOOSER = 2
        const val EXIT_CODE_ERROR_BACKUP_FILE_CREATOR = 3
        const val EXIT_CODE_ERROR_DECRYPTION_ERROR = 7
        const val EXIT_CODE_ERROR_ENCRYPTION_ERROR = 8
        const val EXIT_CODE_ERROR_RESTORE_BACKUP_IS_ENCRYPTED = 9
        const val EXIT_CODE_ERROR_RESTORE_NO_BACKUPS_AVAILABLE = 10
        const val EXIT_CODE_ERROR_ROOM_DATABASE_MISSING = 11
        const val EXIT_CODE_ERROR_STORAGE_PERMISSONS_NOT_GRANTED = 12
        const val EXIT_CODE_ERROR_WRONG_DECRYPTION_PASSWORD = 13
    }
}
