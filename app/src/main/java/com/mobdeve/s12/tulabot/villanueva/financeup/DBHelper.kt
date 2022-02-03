package com.mobdeve.s12.tulabot.villanueva.financeup

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s12.tulabot.villanueva.financeup.model.Transaction
import java.lang.Exception
import java.sql.Blob
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DBHelper(var context: Context?) :
    SQLiteOpenHelper(context, DATABASENAME, null, DATABASEVERSION){

    override fun onCreate(db: SQLiteDatabase){
        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_TRANSACTION_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLEUSERS)
        db.execSQL("DROP TABLE IF EXISTS " + TABLETRANSACTIONS)
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

        const val TABLETRANSACTIONS = "transactions"
        const val USERID = "userid"
        const val TYPE = "type"
        const val TRANSDATE = "transDate"
        const val AMOUNT = "amount"
        const val CATEGORY = "category"
        const val NOTE = "note"
        const val IMGNOTE = "imageNote"

        private const val CREATE_USER_TABLE = ("create table " + TABLEUSERS + "("
                + USERSID + " integer primary key autoincrement, "
                + USERSNAME + " text, "
                + USERSPASS + " text ) ")
        private const val CREATE_TRANSACTION_TABLE = ("create table " + TABLETRANSACTIONS + "("
                + "_id integer primary key autoincrement, "
                + USERID + " integer, "
                + TYPE + " text, "
                + TRANSDATE + " text, "
                + AMOUNT + " float, "
                + CATEGORY + " text, "
                + NOTE + " text, "
                + IMGNOTE + " blob ) ")
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

    fun insertTransaction(userid: Int?, type: String?, transDate: String, amount: Float, category: String, note: String, imageNote: ByteArray?){
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(USERID, userid)
        cv.put(TYPE, type)
        cv.put(TRANSDATE, transDate)
        cv.put(AMOUNT, amount)
        cv.put(CATEGORY, category)
        cv.put(NOTE, note)
        cv.put(IMGNOTE, imageNote)
        db.insert(TABLETRANSACTIONS, null, cv)

        db.close()
    }

    fun editTransaction(tid: Int, userid: Int?, type: String?, transDate: String, amount: Float, category: String, note: String, imageNote: ByteArray?){
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(TYPE, type)
        cv.put(TRANSDATE, transDate)
        cv.put(AMOUNT, amount)
        cv.put(CATEGORY, category)
        cv.put(NOTE, note)
        cv.put(IMGNOTE, imageNote)

        db.update("transactions", cv, "userid = ? and _id = ?", arrayOf(userid.toString(), tid.toString()))

        db.close()
    }

    fun getIncomeTransactions(userid: Int?): Float{
        val db = this.readableDatabase
        val query = "Select SUM(amount) from transactions where userid = ? and type = ?"
        val result = db.rawQuery(query, arrayOf(userid.toString(), "Income"))

        var retRes = 0F
        if(result.moveToFirst()){
            retRes = result.getFloat(0)
        }

        return retRes
    }

    fun getExpenseTransactions(userid: Int?): Float{
        val db = this.readableDatabase
        val query = "Select SUM(amount) from transactions where userid = ? and type = ?"
        val result = db.rawQuery(query, arrayOf(userid.toString(), "Expense"))

        var retRes = 0F
        if(result.moveToFirst()){
            retRes = result.getFloat(0)
        }

        return retRes
    }

    fun getDashboardTransactions(userid: Int?): ArrayList<Transaction?>{
        val cal = Calendar.getInstance();
        val sdf = SimpleDateFormat("yyyy-MM-dd");
        val date = sdf.format(cal.getTime())
        
        val db = this.readableDatabase
        val query = "Select * from transactions where userid = ? and transdate = ? order by _id desc"
        val cursor: Cursor = db.rawQuery(query, arrayOf(userid.toString(), date))
        var transactionList: ArrayList<Transaction?> = ArrayList();

        try{
            if(cursor.moveToFirst()){
                do{
                    val id = cursor.getString(cursor.getColumnIndexOrThrow("_id"))
                    val userid = cursor.getString(cursor.getColumnIndexOrThrow(USERID))
                    val type = cursor.getString(cursor.getColumnIndexOrThrow(TYPE))
                    val transdate = cursor.getString(cursor.getColumnIndexOrThrow(TRANSDATE))
                    val amount = cursor.getString(cursor.getColumnIndexOrThrow(AMOUNT))
                    val category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY))
                    val note = cursor.getString(cursor.getColumnIndexOrThrow(NOTE))
                    val imageNote = cursor.getBlob((cursor.getColumnIndexOrThrow(IMGNOTE)))

                    transactionList.add(Transaction(id.toInt(),userid.toInt(),
                        type,transdate,amount.toFloat(), category,note,imageNote))
                } while(cursor.moveToNext())
            }
        } catch (e: Exception){
            Log.d("DATABASE ERROR", "Error while trying to get all transactions of user from database" )
        } finally {
            if(!cursor.isClosed()){
                cursor.close()
            }
        }

        return transactionList
    }

    fun getFilterTransactions(userid: Int?, type: String, category: String, from: String, to: String): ArrayList<Transaction?>{
        val db = this.readableDatabase
        var query = "Select * from transactions where userid = ? and type = ? and category = ? and transDate between ? and ? order by transDate desc, _id desc"
        var cursor: Cursor = db.rawQuery(query, arrayOf(userid.toString(), type, category, from, to))
        var transactionList: ArrayList<Transaction?> = ArrayList();

        if(category == "All"){
            query = "Select * from transactions where userid = ? and type = ? and transDate between ? and ? order by transDate desc, _id desc"
            cursor = db.rawQuery(query, arrayOf(userid.toString(), type, from, to))
        }

        try{
            if(cursor.moveToFirst()){
                do{
                    val id = cursor.getString(cursor.getColumnIndexOrThrow("_id"))
                    val userid = cursor.getString(cursor.getColumnIndexOrThrow(USERID))
                    val type = cursor.getString(cursor.getColumnIndexOrThrow(TYPE))
                    val transdate = cursor.getString(cursor.getColumnIndexOrThrow(TRANSDATE))
                    val amount = cursor.getString(cursor.getColumnIndexOrThrow(AMOUNT))
                    val category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY))
                    val note = cursor.getString(cursor.getColumnIndexOrThrow(NOTE))
                    val imageNote = cursor.getBlob((cursor.getColumnIndexOrThrow(IMGNOTE)))

                    transactionList.add(Transaction(id.toInt(),userid.toInt(),
                        type,transdate,amount.toFloat(), category,note,imageNote))
                } while(cursor.moveToNext())
            }
        } catch (e: Exception){
            Log.d("DATABASE ERROR", "Error while trying to get all transactions of user from database" )
        } finally {
            if(!cursor.isClosed()){
                cursor.close()
            }
        }

        return transactionList
    }

    // transaction table columns - _id, USERID, TYPE, TRANSDATE. AMOUNT, CATEGORY, NOTE
    fun getAllTransactions(userid: Int?, type:String, from: String, to: String): ArrayList<Transaction?>{
        val db = this.readableDatabase
        var query = "Select * from transactions where userid = ? and type = ? and transDate between ? and ? order by transDate desc, _id desc"
        var cursor: Cursor = db.rawQuery(query, arrayOf(userid.toString(), type, from, to))
        var transactionList: ArrayList<Transaction?> = ArrayList();

        if(type == "All"){
            query = "Select * from transactions where userid = ? and transDate between ? and ? order by transDate desc, _id desc"
            cursor = db.rawQuery(query, arrayOf(userid.toString(), from, to))
        }

        try{
            if(cursor.moveToFirst()){
                do{
                    val id = cursor.getString(cursor.getColumnIndexOrThrow("_id"))
                    val userid = cursor.getString(cursor.getColumnIndexOrThrow(USERID))
                    val type = cursor.getString(cursor.getColumnIndexOrThrow(TYPE))
                    val transdate = cursor.getString(cursor.getColumnIndexOrThrow(TRANSDATE))
                    val amount = cursor.getString(cursor.getColumnIndexOrThrow(AMOUNT))
                    val category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY))
                    val note = cursor.getString(cursor.getColumnIndexOrThrow(NOTE))
                    val imageNote = cursor.getBlob((cursor.getColumnIndexOrThrow(IMGNOTE)))

                    transactionList.add(Transaction(id.toInt(),userid.toInt(),
                        type,transdate,amount.toFloat(), category,note, imageNote))
                } while(cursor.moveToNext())
            }
        } catch (e: Exception){
            Log.d("DATABASE ERROR", "Error while trying to get all transactions of user from database" )
        } finally {
            if(!cursor.isClosed()){
                cursor.close()
            }
        }

        return transactionList
    }

    fun resetData(userid: Int?){
        val db = this.writableDatabase
        db.delete(TABLETRANSACTIONS, "$USERID=?", arrayOf(userid.toString()))

        db.close()
    }

    fun dateFormat(rawdate: Calendar): String{
        val myFormat = "MMMM dd, yyyy"
        val sdf = SimpleDateFormat(myFormat)
        val date = sdf.format(rawdate.getTime())

        return date
    }
}