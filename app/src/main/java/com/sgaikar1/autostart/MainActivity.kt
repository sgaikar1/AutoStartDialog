package com.sgaikar1.autostart

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.sgaikar1.autostartdialog.AutostartDialog
import com.sgaikar1.autostartdialog.AutostartDialogListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv).setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        AutostartDialog.Builder(this)
            .openSettingsWithoutShowingDialog(true)
            .setTitle("Allow AutoStart")
            .setMessage("Please enable auto start in settings.")
            .setPositiveBtnText("Allow")
            .setNegativeBtnText("No")
            .setCancelableOnTouchOutside(false)
            .onPositiveClicked(object: AutostartDialogListener {
                override fun onClick(input: String) {
                  Toast.makeText(this@MainActivity,input,Toast.LENGTH_SHORT).show()
                }
            })
            .onNegativeClicked(object: AutostartDialogListener {
                override fun onClick(input: String) {
                    Toast.makeText(this@MainActivity,"Negative clicked",Toast.LENGTH_SHORT).show()
                }
            })
            .setTitleTextColor(ContextCompat.getColor(this, android.R.color.black))
            .setMessageTextColor(ContextCompat.getColor(this, android.R.color.black))
            .setButtonTextColor(ContextCompat.getColor(this, android.R.color.black))
            .build()
    }
}
