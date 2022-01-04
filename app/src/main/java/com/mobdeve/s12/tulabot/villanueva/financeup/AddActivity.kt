package com.mobdeve.s12.tulabot.villanueva.financeup

import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.TextView
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ActivityAddBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddActivity : AppCompatActivity() {
    var binding: ActivityAddBinding? = null
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // receive data using bundle
        val bundle = intent.extras
        val type = bundle!!.getString("type")

        binding!!.tvAddHeader.text = "Add $type"

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

        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat)
        binding!!.etDate.setText(sdf.format(cal.getTime()), TextView.BufferType.EDITABLE)

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        binding!!.etDate.setOnClickListener {
            val dpd =
                DatePickerDialog(this@AddActivity, DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
                    binding!!.etDate.setText("" + (month+1) + "/" + dayOfMonth + "/" + year)
                }, year, month, day)
            dpd.show()
        }

        binding!!.tvPhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }

        binding!!.btnCancel.setOnClickListener {
            val gotoDashboardActivity = Intent(applicationContext, DashboardActivity:: class.java)

            startActivity(gotoDashboardActivity)
            finish()
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