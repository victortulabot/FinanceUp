package com.mobdeve.s12.tulabot.villanueva.financeup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ActivityDashboardBinding
import com.mobdeve.s12.tulabot.villanueva.financeup.util.SharePrefUtility

class DashboardActivity : AppCompatActivity() {
    var binding: ActivityDashboardBinding? = null
    lateinit var sharedPrefUtility: SharePrefUtility

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        var db = DBHelper(applicationContext)
        sharedPrefUtility = SharePrefUtility(this)

//        val getIncome = db.getIncomeTransactions(sharedPrefUtility.getIntegerPreferences("id")).toString()
//
//        binding!!.tvIncome.text = "₱$getIncome"

        var income = binding!!.tvIncome.text.toString().split("₱");

        var expense = binding!!.tvExpense.text.toString().split("₱");

        binding!!.tvBalance.text = "₱" + ((income[1]).toFloat() - (expense[1]).toFloat()).toString();

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