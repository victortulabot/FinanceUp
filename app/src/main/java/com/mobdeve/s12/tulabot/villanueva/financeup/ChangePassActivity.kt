package com.mobdeve.s12.tulabot.villanueva.financeup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ActivityChangePassBinding
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ActivityMainBinding

class ChangePassActivity : AppCompatActivity() {
    var binding: ActivityChangePassBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangePassBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.btnChangePassword.setOnClickListener {
            val curPass = binding!!.etCurrentPassword.text.toString()
            val newPass = binding!!.etNewPassword.text.toString()
            val confirmNewPass = binding!!.etConfirmNewPassword.text.toString()

            if(curPass == "" || newPass == "" || confirmNewPass == ""){
                Toast.makeText(
                    applicationContext,
                    "Please input all required fields!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if(newPass != confirmNewPass){
                    Toast.makeText(
                        applicationContext,
                        "New Password does not match!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Password changed successfully!",
                        Toast.LENGTH_LONG
                    ).show()

                    val gotoSettingsActivity = Intent(applicationContext, SettingsActivity:: class.java)

                    startActivity(gotoSettingsActivity)
                    finish()
                }
            }
        }

        binding!!.btnHome.setOnClickListener{
            val gotoDashboardActivity = Intent(applicationContext, DashboardActivity:: class.java)

            startActivity(gotoDashboardActivity)
            finish()
        }

        binding!!.btnSettings.setOnClickListener {
            val gotoSettingsActivity = Intent(applicationContext, SettingsActivity:: class.java)

            startActivity(gotoSettingsActivity)
            finish()
        }
    }
}