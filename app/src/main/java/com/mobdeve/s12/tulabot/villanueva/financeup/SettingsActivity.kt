package com.mobdeve.s12.tulabot.villanueva.financeup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ActivitySettingsBinding
import com.mobdeve.s12.tulabot.villanueva.financeup.util.SharePrefUtility

class SettingsActivity : AppCompatActivity() {
    var binding: ActivitySettingsBinding? = null
    lateinit var sharedPrefUtility: SharePrefUtility

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        var db = DBHelper(applicationContext)
        sharedPrefUtility = SharePrefUtility(this)
        val userid = sharedPrefUtility.getIntegerPreferences("id")

        binding!!.tvUsername.text = "Hi, " + sharedPrefUtility.getStringPreferences("username") + "!"

        // set onclick listeners
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

        binding!!.btnReset.setOnClickListener {
            db.resetData(userid)
            Toast.makeText(applicationContext,
                "Your data has been successfully reset!",
                Toast.LENGTH_SHORT).show()
        }

        // bottom app buttons
        binding!!.btnHome.setOnClickListener{
            val gotoDashboardActivity = Intent(applicationContext, DashboardActivity:: class.java)

            startActivity(gotoDashboardActivity)
            finish()
        }

        binding!!.btnTransactions.setOnClickListener {
            val gotoTransactionsActivity = Intent(applicationContext, TransactionsActivity:: class.java)

            startActivity(gotoTransactionsActivity)
            finish()
        }
    }
}