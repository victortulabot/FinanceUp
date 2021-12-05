package com.mobdeve.s12.tulabot.villanueva.financeup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    var binding: ActivityAddBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }
}