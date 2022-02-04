package com.mobdeve.s12.tulabot.villanueva.financeup

import android.R
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s12.tulabot.villanueva.financeup.adapter.TransactionAdapter
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ActivityTransactionsBinding
import com.mobdeve.s12.tulabot.villanueva.financeup.model.Transaction
import com.mobdeve.s12.tulabot.villanueva.financeup.util.SharePrefUtility
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionsActivity : AppCompatActivity() {
    var binding: ActivityTransactionsBinding? = null
    lateinit var sharedPrefUtility: SharePrefUtility
    var transactionAdapter: TransactionAdapter? = null
    var transactionList: ArrayList<Transaction?> = ArrayList()
    var cal = Calendar.getInstance()
    var category = "All"
    var type = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        var db = DBHelper(applicationContext)
        sharedPrefUtility = SharePrefUtility(this)
        val userid = sharedPrefUtility.getIntegerPreferences("id")

        // set date range
        var sdf = SimpleDateFormat("yyyy-MM-dd")
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        binding!!.etFromdate.setText(sdf.format(cal.getTime()), TextView.BufferType.EDITABLE)
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        binding!!.etTodate.setText(sdf.format(cal.getTime()), TextView.BufferType.EDITABLE)

        // get transaction list using adapter
        transactionList = db.getAllTransactions(userid, type, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
        transactionAdapter = TransactionAdapter(applicationContext, transactionList)

        binding!!.transactionsList.layoutManager = LinearLayoutManager(applicationContext,
            LinearLayoutManager.VERTICAL, false)
        binding!!.transactionsList.adapter = transactionAdapter

        // get income and expense, balance
        var getIncome = db.getAllTransactionsAmount(userid, "Income", binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
        var getExpense = db.getAllTransactionsAmount(userid, "Expense", binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())

        binding!!.tvIncome.text = "₱$getIncome"
        binding!!.tvExpense.text = "₱$getExpense"

        var income = binding!!.tvIncome.text.toString().split("₱");
        var expense = binding!!.tvExpense.text.toString().split("₱");
        binding!!.tvBalance.text = "₱" + ((income[1]).toFloat() - (expense[1]).toFloat()).toString();

        val spinner = binding!!.transactionCategory
        spinner.visibility = View.INVISIBLE

        var List: ArrayList<String> = ArrayList<String>();
        var adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, List);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter)

        // set onchange listeners
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // get transaction list using adapter
                if (parent != null) {
                    category = parent.getItemAtPosition(position).toString()
                    transactionList = db.getFilterTransactions(userid, type, category, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                    transactionAdapter = TransactionAdapter(applicationContext, transactionList)

                    binding!!.transactionsList.layoutManager = LinearLayoutManager(applicationContext,
                        LinearLayoutManager.VERTICAL, false)
                    binding!!.transactionsList.adapter = transactionAdapter

                    // get income and expense, balance
                    if((type == "Income")&&(category == "All")){
                        getIncome = db.getFilterTransactionsAmount(userid, "Income", category, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                        binding!!.tvIncome.text = "₱$getIncome"
                        binding!!.tvExpense.text = "₱0"
                    } else if ((type == "Expense")&&(category == "All")){
                        getExpense = db.getFilterTransactionsAmount(userid, "Expense", category, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                        binding!!.tvIncome.text = "₱0"
                        binding!!.tvExpense.text = "₱$getExpense"
                    } else{
                        getIncome = db.getFilterTransactionsAmount(userid, "Income", category, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                        getExpense = db.getFilterTransactionsAmount(userid, "Expense", category, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                        binding!!.tvIncome.text = "₱$getIncome"
                        binding!!.tvExpense.text = "₱$getExpense"
                    }

                    income = binding!!.tvIncome.text.toString().split("₱");
                    expense = binding!!.tvExpense.text.toString().split("₱");
                    binding!!.tvBalance.text = "₱" + ((income[1]).toFloat() - (expense[1]).toFloat()).toString();

                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // interface callback
            }
        }

        binding!!.etFromdate.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {

                var transactionList =  db.getAllTransactions(userid,type, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                getIncome = db.getAllTransactionsAmount(userid, "Income", binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                getExpense = db.getAllTransactionsAmount(userid, "Expense", binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())

                if(spinner.isShown){
                    transactionList = db.getFilterTransactions(userid, type, category, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                    if((type == "Income")&&(category == "All")){
                        getIncome = db.getFilterTransactionsAmount(userid, "Income", category, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                        binding!!.tvIncome.text = "₱$getIncome"
                        binding!!.tvExpense.text = "₱0"
                    } else if ((type == "Expense")&&(category == "All")){
                        getExpense = db.getFilterTransactionsAmount(userid, "Expense", category, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                        binding!!.tvIncome.text = "₱0"
                        binding!!.tvExpense.text = "₱$getExpense"
                    } else{
                        getIncome = db.getFilterTransactionsAmount(userid, "Income", category, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                        getExpense = db.getFilterTransactionsAmount(userid, "Expense", category, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                        binding!!.tvIncome.text = "₱$getIncome"
                        binding!!.tvExpense.text = "₱$getExpense"
                    }
                } else{
                    binding!!.tvIncome.text = "₱$getIncome"
                    binding!!.tvExpense.text = "₱$getExpense"
                }

                income = binding!!.tvIncome.text.toString().split("₱");
                expense = binding!!.tvExpense.text.toString().split("₱");
                binding!!.tvBalance.text = "₱" + ((income[1]).toFloat() - (expense[1]).toFloat()).toString();

                transactionAdapter = TransactionAdapter(applicationContext, transactionList)
                binding!!.transactionsList.layoutManager = LinearLayoutManager(applicationContext,
                    LinearLayoutManager.VERTICAL, false)
                binding!!.transactionsList.adapter = transactionAdapter
            }
        })

        binding!!.etTodate.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                var transactionList =  db.getAllTransactions(userid,type, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                getIncome = db.getAllTransactionsAmount(userid, "Income", binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                getExpense = db.getAllTransactionsAmount(userid, "Expense", binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())

                if(spinner.isShown){
                    transactionList = db.getFilterTransactions(userid, type, category, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                    if((type == "Income")&&(category == "All")){
                        getIncome = db.getFilterTransactionsAmount(userid, "Income", category, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                        binding!!.tvIncome.text = "₱$getIncome"
                        binding!!.tvExpense.text = "₱0"
                    } else if ((type == "Expense")&&(category == "All")){
                        getExpense = db.getFilterTransactionsAmount(userid, "Expense", category, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                        binding!!.tvIncome.text = "₱0"
                        binding!!.tvExpense.text = "₱$getExpense"
                    } else{
                        getIncome = db.getFilterTransactionsAmount(userid, "Income", category, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                        getExpense = db.getFilterTransactionsAmount(userid, "Expense", category, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
                        binding!!.tvIncome.text = "₱$getIncome"
                        binding!!.tvExpense.text = "₱$getExpense"
                    }
                } else{
                    binding!!.tvIncome.text = "₱$getIncome"
                    binding!!.tvExpense.text = "₱$getExpense"
                }

                income = binding!!.tvIncome.text.toString().split("₱");
                expense = binding!!.tvExpense.text.toString().split("₱");
                binding!!.tvBalance.text = "₱" + ((income[1]).toFloat() - (expense[1]).toFloat()).toString();

                transactionAdapter = TransactionAdapter(applicationContext, transactionList)
                binding!!.transactionsList.layoutManager = LinearLayoutManager(applicationContext,
                    LinearLayoutManager.VERTICAL, false)
                binding!!.transactionsList.adapter = transactionAdapter
            }
        })

        // set onclick listeners
        binding!!.etFromdate.setOnClickListener {
            cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val dpd =
                DatePickerDialog(this@TransactionsActivity, DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
                    cal.set(year,month,dayOfMonth)
                    val pickedNewDate = sdf.format(cal.getTime())
                    binding!!.etFromdate.setText(pickedNewDate)
                }, year, month, day)
            dpd.show()
        }

        binding!!.etTodate.setOnClickListener {
            cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val dpd =
                DatePickerDialog(this@TransactionsActivity, DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
                    cal.set(year,month,dayOfMonth)
                    val pickedNewDate = sdf.format(cal.getTime())
                    binding!!.etTodate.setText(pickedNewDate)
                }, year, month, day)
            dpd.show()
        }

        binding!!.btnAll.setOnClickListener {
            type = "All"
            spinner.visibility = View.INVISIBLE
            binding!!.btnAll.backgroundTintList =
                getColorStateList(R.color.darker_gray)
            binding!!.btnIncome.backgroundTintList =
                getColorStateList(R.color.white)
            binding!!.btnExpense.backgroundTintList =
                getColorStateList(R.color.white)

            // get transaction list using adapter
            transactionList = db.getAllTransactions(userid,type, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
            transactionAdapter = TransactionAdapter(applicationContext, transactionList)

            binding!!.transactionsList.layoutManager = LinearLayoutManager(applicationContext,
                LinearLayoutManager.VERTICAL, false)
            binding!!.transactionsList.adapter = transactionAdapter

            // get income and expense, balance
            getIncome = db.getAllTransactionsAmount(userid, "Income", binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
            getExpense = db.getAllTransactionsAmount(userid, "Expense", binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())

            binding!!.tvIncome.text = "₱$getIncome"
            binding!!.tvExpense.text = "₱$getExpense"

            income = binding!!.tvIncome.text.toString().split("₱");
            expense = binding!!.tvExpense.text.toString().split("₱");
            binding!!.tvBalance.text = "₱" + ((income[1]).toFloat() - (expense[1]).toFloat()).toString();
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
            transactionList = db.getAllTransactions(userid, type, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
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
            transactionList = db.getAllTransactions(userid, type, binding!!.etFromdate.text.toString(), binding!!.etTodate.text.toString())
            transactionAdapter = TransactionAdapter(applicationContext, transactionList)

            binding!!.transactionsList.layoutManager = LinearLayoutManager(applicationContext,
                LinearLayoutManager.VERTICAL, false)
            binding!!.transactionsList.adapter = transactionAdapter

        }

        // bottom app buttons
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