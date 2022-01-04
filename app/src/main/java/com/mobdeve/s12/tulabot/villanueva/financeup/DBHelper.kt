package com.mobdeve.s12.tulabot.villanueva.financeup

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DBHelper(var context: Context?) :
    SQLiteOpenHelper(context, DATABASENAME, null, DATABASEVERSION){

    override fun onCreate(db: SQLiteDatabase){
        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_TRANSACTION_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLEUSERS)
        onCreate(db)
    }

    companion object {
        private const val DATABASENAME = "FinanceUp.db"
        private const val DATABASEVERSION = 1

        //column names
        const val TABLEUSERS = "users"
        const val USERSID = "_id"
        const val USERSNAME = "username"
        const val USERSPASS = "userpass"
        private const val CREATE_USER_TABLE = ("create table " + TABLEUSERS + "("
                + USERSID + " integer primary key autoincrement, "
                + USERSNAME + " text, "
                + USERSPASS + " text ) ")
        private const val CREATE_TRANSACTION_TABLE = ("create table transactions ("
                + "_id integer primary key autoincrement, "
                + "userid integer, "
                + "type text, "
                + "date date, "
                + "amount float, "
                + "category text, "
                + "note text ) ")
    }

    fun insertDataUser(username: String, password: String){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(USERSNAME, username)
        cv.put(USERSPASS, password)
        db.insert(TABLEUSERS, null, cv)

        db.close()
    }

    fun checkDataUser(username: String):Boolean{
        var boolResult = false
        val db = this.writableDatabase
        val query = "Select * from users where username = ?"
        val result:Cursor = db.rawQuery(query, arrayOf(username) )

        if (result.count > 0){
            boolResult = true
        }

        result.close()
        db.close()

        return boolResult
    }

    fun loginDataUser(username: String, password: String):Boolean{
        var boolResult = false
        val db = this.readableDatabase
        val query = "Select * from users where username = ? and userpass = ?"
        val result:Cursor = db.rawQuery(query, arrayOf(username,password) )

        if (result.count > 0){
            boolResult = true
        }

        result.close()
        db.close()

        return boolResult
    }

    fun getUserID(username: String): Int{
        val db = this.readableDatabase
        val query = "Select _id from users where username = ?"
        val result = db.rawQuery(query, arrayOf(username))

        var retRes = 0

        if(result.moveToFirst()){
            retRes = result.getInt(0)
        }

        return retRes
    }

    fun getUserPassword(username: String?): String{
        val db = this.readableDatabase
        val query = "Select userpass from users where username = ?"
        val result = db.rawQuery(query, arrayOf(username))

        var retRes = ""

        if(result.moveToFirst()){
            retRes = result.getString(0)
        }

        return retRes
    }

    fun changeUserPassword(username: String?, password: String){
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(USERSPASS, password)

        db.update("users", cv, "username = ?", arrayOf(username))

        db.close()
    }
}