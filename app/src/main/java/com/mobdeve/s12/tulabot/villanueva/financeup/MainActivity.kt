package com.mobdeve.s12.tulabot.villanueva.financeup

import android.content.Intent
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ActivityMainBinding
import com.mobdeve.s12.tulabot.villanueva.financeup.util.SharePrefUtility

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    lateinit var sharedPrefUtility: SharePrefUtility

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        var db = DBHelper(applicationContext)
        sharedPrefUtility = SharePrefUtility(this)

        if(sharedPrefUtility.getBoolPreferences("isLoggedIn")){
            val gotoDashboardActivity = Intent(applicationContext, DashboardActivity:: class.java)

            startActivity(gotoDashboardActivity)
            finish()
        } else {
            binding!!.btnLog.setOnClickListener {
                binding!!.etConfirmPassword.visibility = View.INVISIBLE;
                binding!!.btnRegister.backgroundTintList = getColorStateList(android.R.color.white);
                binding!!.btnLog.backgroundTintList =
                    getColorStateList(android.R.color.darker_gray);
                binding!!.btnLogin.text = "Log In";
            }

            binding!!.btnRegister.setOnClickListener {
                binding!!.etConfirmPassword.visibility = View.VISIBLE;
                binding!!.btnLog.backgroundTintList = getColorStateList(android.R.color.white);
                binding!!.btnRegister.backgroundTintList =
                    getColorStateList(android.R.color.darker_gray);
                binding!!.btnLogin.text = "Register";
            }

            binding!!.btnLogin.setOnClickListener {
                val username = binding!!.etUsername.text.toString();
                val password = binding!!.etPassword.text.toString();
                val cPassword = binding!!.etConfirmPassword.text.toString();

                if (binding!!.btnLogin.text.toString() == "Register") {
                    if (username == "" || password == "" || cPassword == "") {
                        Toast.makeText(
                            applicationContext,
                            "Please input all required fields!",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (password != cPassword) {
                        Toast.makeText(
                            applicationContext,
                            "Password does not match!",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        if (db.checkDataUser(username)) {
                            Toast.makeText(
                                applicationContext,
                                "Username already exists!",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            db.insertDataUser(username, password);
                            sharedPrefUtility.saveBoolPreferences("isLoggedIn", true)
                            sharedPrefUtility.saveStringPreferences("username", username)
                            sharedPrefUtility.saveIntegerPreferences("id", db.getUserID(username))

                            val gotoDashboardActivity =
                                Intent(applicationContext, DashboardActivity::class.java)

                            startActivity(gotoDashboardActivity)
                            finish()
                        }
                    }
                } else {
                    if (username == "" || password == "") {
                        Toast.makeText(
                            applicationContext,
                            "Please input all required fields!",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        if (db.loginDataUser(username, password)) {
                            sharedPrefUtility.saveBoolPreferences("isLoggedIn", true)
                            sharedPrefUtility.saveStringPreferences("username", username)
                            sharedPrefUtility.saveIntegerPreferences("id", db.getUserID(username))

                            val gotoDashboardActivity =
                                Intent(applicationContext, DashboardActivity::class.java)

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
}