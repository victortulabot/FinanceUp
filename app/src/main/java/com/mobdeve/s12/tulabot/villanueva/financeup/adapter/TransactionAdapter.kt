package com.mobdeve.s12.tulabot.villanueva.financeup.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.tulabot.villanueva.financeup.AddActivity
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ItemTransactionRowBinding
import com.mobdeve.s12.tulabot.villanueva.financeup.model.Transaction
import java.text.SimpleDateFormat

class TransactionAdapter(private val context: Context,
                         private var transactionList: ArrayList<Transaction?>
)
    : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemTransactionRowBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount() = transactionList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTransaction(transactionList[position]!!)
        holder.viewTransaction(transactionList[position]!!)
    }

    class ViewHolder(private val itemBinding: ItemTransactionRowBinding)
        :RecyclerView.ViewHolder(itemBinding.root){

        // set the data
        fun bindTransaction(transaction: Transaction){
            itemBinding.category.text = transaction.category

            // format date
            val date = transaction.transdate
            val sdf = SimpleDateFormat("MMMM dd, yyyy");
            val sdf2 = SimpleDateFormat("yyyy-MM-dd");
            val p_date = sdf2.parse(date)
            val f_date = sdf.format(p_date)
            itemBinding.transdate.text = f_date

            // change color of text based on type
            if(transaction.type == "Income"){
                itemBinding.transamount.setTextColor(Color.parseColor("#26A910"))
                itemBinding.transamount.text = "+${transaction.amount}"
            }else{
                itemBinding.transamount.setTextColor(Color.parseColor("#AF2222"))
                itemBinding.transamount.text = "-${transaction.amount}"
            }

            // set picture based on category
        }

        fun viewTransaction(transaction: Transaction){
            itemBinding.transCardView.setOnClickListener{
                val gotoAddActivity = Intent(itemBinding.root.context, AddActivity:: class.java)

                var bundle = Bundle()
                bundle.putString("action", "view")
                bundle.putInt("tid", transaction.t_id)
                bundle.putString("type", transaction.type)
                bundle.putString("transDate", transaction.transdate)
                bundle.putFloat("amount", transaction.amount)
                bundle.putString("category", transaction.category)
                bundle.putString("note", transaction.note)
                bundle.putByteArray("imageNote", transaction.imagenote)
                gotoAddActivity.putExtras(bundle)

                itemBinding.root.context.startActivity(gotoAddActivity)
            }
        }
    }
}