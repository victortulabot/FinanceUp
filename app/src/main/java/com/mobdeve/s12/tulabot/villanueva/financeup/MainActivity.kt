package com.mobdeve.s12.tulabot.villanueva.financeup

import android.content.Intent
import android.database.DatabaseUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        var db = DBHelper(this)

        binding!!.btnLog.setOnClickListener {
            binding!!.etConfirmPassword.visibility = View.INVISIBLE;
            binding!!.btnRegister.backgroundTintList = getColorStateList(android.R.color.white);
            binding!!.btnLog.backgroundTintList = getColorStateList(android.R.color.darker_gray);
            binding!!.btnLogin.text = "Log In";
        }

        binding!!.btnRegister.setOnClickListener {
            binding!!.etConfirmPassword.visibility = View.VISIBLE;
            binding!!.btnLog.backgroundTintList = getColorStateList(android.R.color.white);
            binding!!.btnRegister.backgroundTintList = getColorStateList(android.R.color.darker_gray);
            binding!!.btnLogin.text = "Register";
        }

        binding!!.btnLogin.setOnClickListener{
            val username = binding!!.etUsername.text.toString();
            val password = binding!!.etPassword.text.toString();
            val cPassword = binding!!.etConfirmPassword.text.toString();

            if (binding!!.btnLogin.text.toString() == "Register"){
                if (username == "" || password == "" || cPassword == ""){
                    Toast.makeText(
                        applicationContext,
                        "Please input all required fields!",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (password != cPassword){
                    Toast.makeText(
                        applicationContext,
                        "Password does not match!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    if(db.checkDataUser(username)){
                        Toast.makeText(
                            applicationContext,
                            "Username already exists!",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        db.insertDataUser(username, password);

                        val gotoDashboardActivity = Intent(applicationContext, DashboardActivity:: class.java)

                        startActivity(gotoDashboardActivity)
                        finish()
                    }
                }
            }
            else {
                if (username == "" || password == ""){
                    Toast.makeText(
                        applicationContext,
                        "Please input all required fields!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
//                    var log = db.loginDataUser(username, password)
                    var log = true
                    if (log){
                        val gotoDashboardActivity = Intent(applicationContext, DashboardActivity:: class.java)

                        startActivity(gotoDashboardActivity)
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Invalid credentials!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            }
        }
    }
}