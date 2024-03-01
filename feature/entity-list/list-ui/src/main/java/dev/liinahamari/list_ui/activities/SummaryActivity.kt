package dev.liinahamari.list_ui.activities

import androidx.appcompat.app.AppCompatActivity
import dev.liinahamari.list_ui.R

class SummaryActivity : AppCompatActivity(R.layout.activity_summary) {
    override fun finish() {
        setResult(RETURN_CODE_SUCCESS)
        super.finish()
    }
}
