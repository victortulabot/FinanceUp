package com.mobdeve.s12.tulabot.villanueva.financeup

import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ActivityAddBinding
import com.mobdeve.s12.tulabot.villanueva.financeup.util.SharePrefUtility
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddActivity : AppCompatActivity() {
    var binding: ActivityAddBinding? = null
    lateinit var sharedPrefUtility: SharePrefUtility
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        var db = DBHelper(applicationContext)
        sharedPrefUtility = SharePrefUtility(this)

        // receive data using bundle
        val bundle = intent.extras
        val type = bundle!!.getString("type")
        val action = bundle!!.getString("action")
        val sdf = SimpleDateFormat("MMMM dd, yyyy")

        val spinner = binding!!.spinnerCategory
        var List: ArrayList<String> = ArrayList<String>();

        if (type == "Income"){
            List.add("Salary");
            List.add("Carry Over");
        } else {
            List.add("Transportation");
            List.add("Utilities");
            List.add("Entertainment");
            List.add("Clothing");
            List.add("General");
        }

        var adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, List);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if(action.equals("add")){
            binding!!.btnAdd.text = "Add"
            binding!!.tvAddHeader.text = "Add $type"

            binding!!.etDate.setText(sdf.format(cal.getTime()), TextView.BufferType.EDITABLE)
        }
        else if(action.equals("view")){
            binding!!.btnAdd.text = "Edit"
            binding!!.tvAddHeader.text = "View $type"

            // format date
            val date = bundle!!.getString("transDate")
            val sdf = SimpleDateFormat("MMMM dd, yyyy");
            val sdf2 = SimpleDateFormat("yyyy-MM-dd");
            val p_date = sdf2.parse(date)
            val f_date = sdf.format(p_date)
            binding!!.etDate.setText(f_date)
            binding!!.etAmount.setText(bundle!!.getFloat("amount").toString(), TextView.BufferType.EDITABLE)
            var valuePosition = adapter.getPosition(bundle!!.getString("category"))
            spinner.setSelection(valuePosition)
            binding!!.etNote.setText(bundle!!.getString("note"), TextView.BufferType.EDITABLE)
        }

        binding!!.tvPhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }

        binding!!.btnCancel.setOnClickListener {
            val gotoDashboardActivity = Intent(applicationContext, DashboardActivity:: class.java)

            startActivity(gotoDashboardActivity)
            finish()
        }

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        binding!!.etDate.setOnClickListener {
            val dpd =
                DatePickerDialog(this@AddActivity, DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
                    cal.set(year,month,dayOfMonth)
                    val pickedNewDate = sdf.format(cal.getTime())
                    binding!!.etDate.setText(pickedNewDate)
                }, year, month, day)
            dpd.show()
        }

        binding!!.btnAdd.setOnClickListener{
            val userid = sharedPrefUtility.getIntegerPreferences("id")
            val transDate = binding!!.etDate.text.toString()
            val am = binding!!.etAmount.text.toString()
            val category = binding!!.spinnerCategory.selectedItem.toString()
            val note = binding!!.etNote.text.toString()

            // format date
            val sdf2 = SimpleDateFormat("yyyy-MM-dd");
            val p_date = sdf.parse(transDate)
            var f_date = sdf2.format(p_date)

            // check if inputs are not empty
            if (am == "") {
                Toast.makeText(
                    applicationContext,
                    "Please input an amount!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val amount = am.toFloat()

                if(action.equals("add")){
                    db.insertTransaction(userid,type,f_date,amount,category,note)
                    Toast.makeText(applicationContext,
                        "Transaction added successfully!",
                        Toast.LENGTH_SHORT).show()
                }
                else if(action.equals("view")){
                    val tid = bundle!!.getInt("tid")
                    db.editTransaction(tid,userid,type,f_date,amount,category,note)
                    Toast.makeText(applicationContext,
                        "Transaction edited successfully!",
                        Toast.LENGTH_SHORT).show()
                }

                val gotoTransactionsActivity = Intent(applicationContext, TransactionsActivity:: class.java)

                startActivity(gotoTransactionsActivity)
                finish()
            }
        }
    }

    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

}