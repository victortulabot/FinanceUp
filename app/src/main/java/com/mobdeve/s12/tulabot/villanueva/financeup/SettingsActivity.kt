package com.mobdeve.s12.tulabot.villanueva.financeup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    var binding: ActivitySettingsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }
}