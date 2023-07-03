package dev.liinahamari.highlights.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dev.liinahamari.feature.db_backup.impl.RoomBackupTool
import dev.liinahamari.highlights.BuildConfig
import dev.liinahamari.highlights.db.EntriesDatabase
import javax.inject.Named

@Module
class BackupModule {
    @Provides
    fun backupTool(@Named(APP_CONTEXT) context: Context, db: EntriesDatabase): RoomBackupTool =
        RoomBackupTool(context).apply {
            setApplicationId(BuildConfig.APPLICATION_ID)
            setDatabase(db)
        }
}
