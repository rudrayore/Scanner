package com.purnendu.scanner.scanner

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

//This is the custom contract for getting the data after successful scanning
class ScannerActivityContract : ActivityResultContract<Unit, String?>() {

    companion object {
        const val REQUEST_KEY = "my_result_key"
    }

    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, ScannerActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? = when {
        resultCode != Activity.RESULT_OK -> null
        else -> intent?.getStringExtra(REQUEST_KEY)
    }
}