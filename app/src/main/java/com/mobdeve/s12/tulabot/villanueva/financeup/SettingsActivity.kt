package com.mobdeve.s12.tulabot.villanueva.financeup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    var binding: ActivitySettingsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.btnLogout.setOnClickListener{
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