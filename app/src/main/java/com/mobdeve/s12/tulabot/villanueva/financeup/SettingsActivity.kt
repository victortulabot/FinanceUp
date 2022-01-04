package com.mobdeve.s12.tulabot.villanueva.financeup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ActivitySettingsBinding
import com.mobdeve.s12.tulabot.villanueva.financeup.util.SharePrefUtility

class SettingsActivity : AppCompatActivity() {
    var binding: ActivitySettingsBinding? = null
    lateinit var sharedPrefUtility: SharePrefUtility

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        sharedPrefUtility = SharePrefUtility(this)

        binding!!.tvUsername.text = "Hi, " + sharedPrefUtility.getStringPreferences("username") + "!"

        binding!!.btnChangePassword.setOnClickListener {
            val gotoChangePassActivity = Intent(applicationContext, ChangePassActivity:: class.java)

            startActivity(gotoChangePassActivity)
            finish()
        }

        binding!!.btnLogout.setOnClickListener{
            sharedPrefUtility.removeAllPreferences()
            val gotoMainActivity = Intent(applicationContext, MainActivity:: class.java)

            startActivity(gotoMainActivity)
            finish()
        }

        binding!!.btnHome.setOnClickListener{
            val gotoDashboardActivity = Intent(applicationContext, DashboardActivity:: class.java)

            startActivity(gotoDashboardActivity)
            finish()
        }
    }
}