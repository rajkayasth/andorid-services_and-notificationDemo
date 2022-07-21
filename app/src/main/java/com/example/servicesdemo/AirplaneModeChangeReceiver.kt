package com.example.servicesdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.View
import com.google.android.material.snackbar.Snackbar

class AirplaneModeChangeReceiver(private val view: View) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val isAirplaneModeEnable = intent?.getBooleanExtra("state", false) ?: return
        if (isAirplaneModeEnable) {
            val snack = Snackbar.make(view,"Airplane mode Enabled ",Snackbar.LENGTH_SHORT)
            snack.show()
        }else{
            val snack = Snackbar.make(view,"Airplane mode Disable ",Snackbar.LENGTH_SHORT)
            snack.show()
        }
    }
}