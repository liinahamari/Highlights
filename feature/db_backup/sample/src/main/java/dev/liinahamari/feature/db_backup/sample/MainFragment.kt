package dev.liinahamari.feature.db_backup.sample

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import dev.liinahamari.db_backup.sample.BuildConfig
import dev.liinahamari.db_backup.sample.R
import dev.liinahamari.feature.db_backup.impl.RoomBackupTool

class MainFragment : Fragment(R.layout.fragment_main) {
    private var backupTool: RoomBackupTool? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backupTool = RoomBackupTool(this).apply {
            setDatabase((requireActivity().application as App).db)
            setApplicationId(BuildConfig.APPLICATION_ID)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setupOnClickListeners()
    }

    override fun onDetach() {
        backupTool = null
        super.onDetach()
    }

    private fun View.setupOnClickListeners() {
        findViewById<Button>(R.id.backupButton).setOnClickListener {
            backupTool!!.backup()
        }

        findViewById<Button>(R.id.restoreBtn).setOnClickListener {
            backupTool!!.restore()
        }
    }
}
