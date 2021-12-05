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

            startActivity(gotoAddActivity)
            finish()
        }
    }
}