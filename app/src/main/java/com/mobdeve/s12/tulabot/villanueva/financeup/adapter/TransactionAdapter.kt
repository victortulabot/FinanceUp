package com.mobdeve.s12.tulabot.villanueva.financeup.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.tulabot.villanueva.financeup.databinding.ItemTransactionRowBinding
import com.mobdeve.s12.tulabot.villanueva.financeup.model.Transaction

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
    }

    class ViewHolder(private val itemBinding: ItemTransactionRowBinding)
        :RecyclerView.ViewHolder(itemBinding.root){

        // set the data
        fun bindTransaction(transaction: Transaction){
            itemBinding.category.text = transaction.category
            itemBinding.transdate.text = transaction.transdate

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
    }
}