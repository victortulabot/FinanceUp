package com.mobdeve.s12.tulabot.villanueva.financeup.util

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s12.tulabot.villanueva.financeup.DBHelper
import com.mobdeve.s12.tulabot.villanueva.financeup.DashboardActivity
import com.mobdeve.s12.tulabot.villanueva.financeup.SettingsActivity
import com.mobdeve.s12.tulabot.villanueva.financeup.adapter.TransactionAdapter
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ActivityDashboardBinding
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ActivityTransactionsBinding
import com.mobdeve.s12.tulabot.villanueva.financeup.model.Transaction

class TransactionsActivity : AppCompatActivity() {
    var binding: ActivityTransactionsBinding? = null
    lateinit var sharedPrefUtility: SharePrefUtility
    var transactionAdapter: TransactionAdapter? = null
    var transactionList: ArrayList<Transaction?> = ArrayList()
    var type = "Income"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        var db = DBHelper(applicationContext)
        sharedPrefUtility = SharePrefUtility(this)
        val userid = sharedPrefUtility.getIntegerPreferences("id")

        // get transaction list using adapter
        transactionList = db.getAllTransactions(userid,"All")
        transactionAdapter = TransactionAdapter(applicationContext, transactionList)

        binding!!.transactionsList.layoutManager = LinearLayoutManager(applicationContext,
            LinearLayoutManager.VERTICAL, false)
        binding!!.transactionsList.adapter = transactionAdapter

        val spinner = binding!!.transactionCategory
        spinner.visibility = View.INVISIBLE

        var List: ArrayList<String> = ArrayList<String>();
        var adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, List);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter)

        // set onclick listeners
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // get transaction list using adapter
                if (parent != null) {
                    transactionList = db.getFilterTransactions(userid, type, parent.getItemAtPosition(position).toString())
                    transactionAdapter = TransactionAdapter(applicationContext, transactionList)

                    binding!!.transactionsList.layoutManager = LinearLayoutManager(applicationContext,
                        LinearLayoutManager.VERTICAL, false)
                    binding!!.transactionsList.adapter = transactionAdapter

                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // interface callback
            }
        }

        binding!!.btnAll.setOnClickListener {
            spinner.visibility = View.INVISIBLE
            binding!!.btnAll.backgroundTintList =
                getColorStateList(R.color.darker_gray)
            binding!!.btnIncome.backgroundTintList =
                getColorStateList(R.color.white)
            binding!!.btnExpense.backgroundTintList =
                getColorStateList(R.color.white)

            // get transaction list using adapter
            transactionList = db.getAllTransactions(userid,"All")
            transactionAdapter = TransactionAdapter(applicationContext, transactionList)

            binding!!.transactionsList.layoutManager = LinearLayoutManager(applicationContext,
                LinearLayoutManager.VERTICAL, false)
            binding!!.transactionsList.adapter = transactionAdapter
        }
        binding!!.btnIncome.setOnClickListener {
            type="Income"
            spinner.visibility = View.VISIBLE
            binding!!.btnAll.backgroundTintList =
                getColorStateList(R.color.white)
            binding!!.btnIncome.backgroundTintList =
                getColorStateList(R.color.darker_gray)
            binding!!.btnExpense.backgroundTintList =
                getColorStateList(R.color.white)
            List.clear()
            List.add("All")
            List.add("Salary")
            List.add("Carry Over")

            adapter = ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, List);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter)

            // get transaction list using adapter
            transactionList = db.getAllTransactions(userid,"Income")
            transactionAdapter = TransactionAdapter(applicationContext, transactionList)

            binding!!.transactionsList.layoutManager = LinearLayoutManager(applicationContext,
                LinearLayoutManager.VERTICAL, false)
            binding!!.transactionsList.adapter = transactionAdapter
        }

        binding!!.btnExpense.setOnClickListener {
            type="Expense"
            spinner.visibility = View.VISIBLE
            binding!!.btnAll.backgroundTintList =
                getColorStateList(R.color.white)
            binding!!.btnIncome.backgroundTintList =
                getColorStateList(R.color.white)
            binding!!.btnExpense.backgroundTintList =
                getColorStateList(R.color.darker_gray)
            List.clear()
            List.add("All")
            List.add("Transportation")
            List.add("Utilities")
            List.add("Entertainment")
            List.add("Clothing")
            List.add("General")

            adapter = ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, List);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter)

            // get transaction list using adapter
            transactionList = db.getAllTransactions(userid,"Expense")
            transactionAdapter = TransactionAdapter(applicationContext, transactionList)

            binding!!.transactionsList.layoutManager = LinearLayoutManager(applicationContext,
                LinearLayoutManager.VERTICAL, false)
            binding!!.transactionsList.adapter = transactionAdapter
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