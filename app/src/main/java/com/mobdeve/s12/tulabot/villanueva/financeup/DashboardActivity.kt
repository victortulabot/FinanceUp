package com.mobdeve.s12.tulabot.villanueva.financeup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    var binding: ActivityDashboardBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.btnAddIncome.setOnClickListener{
            val gotoAddActivity = Intent(applicationContext, AddActivity:: class.java)

            // pass data using bundle
            var bundle = Bundle()
            bundle.putString("type", "Income")
            gotoAddActivity.putExtras(bundle)

            startActivity(gotoAddActivity)
            finish()
        }

        binding!!.btnAddExpense.setOnClickListener{
            val gotoAddActivity = Intent(applicationContext, AddActivity:: class.java)

            // pass data using bundle
            var bundle = Bundle()
            bundle.putString("type", "Expense")
            gotoAddActivity.putExtras(bundle)

            startActivity(gotoAddActivity)
            finish()
        }

        binding!!.btnSettings.setOnClickListener {
            val gotoSettingsActivity = Intent(applicationContext, SettingsActivity:: class.java)

            startActivity(gotoSettingsActivity)
            finish()
        }
    }
}