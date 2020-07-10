package com.example.scheduledfridge.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledfridge.R
import com.example.scheduledfridge.database.Product
import kotlinx.android.synthetic.main.product_layout.view.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.util.*

class listOfproductsAdapter internal constructor(context: Context?, products: List<Product>): RecyclerView.Adapter<listOfproductsAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    val products = products

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val productName = itemView.findViewById<TextView>(R.id.product_name_textView)
        val daysLeft = itemView.findViewById<TextView>(R.id.daysLeft_textView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.product_layout,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        Log.d("itemCount", products.size.toString())
        return products.size
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = products[position]
        val today= LocalDate.now()
      /*  val expirationDay = current.productExpirationDate as Temporal
        val daysBetween = ChronoUnit.DAYS.between(today,expirationDay)*/

        holder.productName.text = current.productName
        holder.daysLeft.text  = "7 days"
    }

}