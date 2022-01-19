package com.mobdeve.s12.tulabot.villanueva.financeup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceControl
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s12.tulabot.villanueva.financeup.adapter.TransactionAdapter
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ActivityDashboardBinding
import com.mobdeve.s12.tulabot.villanueva.financeup.model.Transaction
import com.mobdeve.s12.tulabot.villanueva.financeup.util.SharePrefUtility
import com.mobdeve.s12.tulabot.villanueva.financeup.util.TransactionsActivity

class DashboardActivity : AppCompatActivity() {
    var binding: ActivityDashboardBinding? = null
    lateinit var sharedPrefUtility: SharePrefUtility
    var transactionAdapter: TransactionAdapter? = null
    var transactionList: ArrayList<Transaction?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        var db = DBHelper(applicationContext)
        sharedPrefUtility = SharePrefUtility(this)

        val userid = sharedPrefUtility.getIntegerPreferences("id")

        val getIncome = db.getIncomeTransactions(userid).toString()
        val getExpense = db.getExpenseTransactions(userid).toString()
//
        binding!!.tvIncome.text = "₱$getIncome"
        binding!!.tvExpense.text = "₱$getExpense"

        var income = binding!!.tvIncome.text.toString().split("₱");

        var expense = binding!!.tvExpense.text.toString().split("₱");

        binding!!.tvBalance.text = "₱" + ((income[1]).toFloat() - (expense[1]).toFloat()).toString();

        // get transaction list using adapter
        transactionList = db.getDashboardTransactions(userid)
        transactionAdapter = TransactionAdapter(applicationContext, transactionList)

        binding!!.transactionlist.layoutManager = LinearLayoutManager(applicationContext,
            LinearLayoutManager.VERTICAL, false)
        binding!!.transactionlist.adapter = transactionAdapter

        // onclick listeners
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

        binding!!.btnTransactions.setOnClickListener {
            val gotoTransactionsActivity = Intent(applicationContext, TransactionsActivity:: class.java)

            startActivity(gotoTransactionsActivity)
            finish()
        }

        binding!!.btnSettings.setOnClickListener {
            val gotoSettingsActivity = Intent(applicationContext, SettingsActivity:: class.java)

            startActivity(gotoSettingsActivity)
            finish()
        }
    }
}