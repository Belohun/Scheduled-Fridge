package com.example.scheduledfridge.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledfridge.R
import com.example.scheduledfridge.database.Product
import org.joda.time.Days
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.coroutines.coroutineContext

class listOfproductsAdapter internal constructor(context: Context?, products: List<Product>): RecyclerView.Adapter<listOfproductsAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    val products = products
    val context = context

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val productName = itemView.findViewById<TextView>(R.id.product_name_textView)
        val daysLeft = itemView.findViewById<TextView>(R.id.daysLeft_textView)
        val quanity = itemView.findViewById<TextView>(R.id.product_quanity_textView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.product_layout,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.animation = AnimationUtils.loadAnimation(context,R.anim.fade_scale)
        val current = products[position]
        var formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
        val today= LocalDate.now()
        val toDate = LocalDate.parse(current.productExpirationDate,formatter)
        val daysBetween = ChronoUnit.DAYS.between(today,toDate)
        holder.productName.text = current.productName
        if(daysBetween<=0){
            holder.daysLeft.text="Expired"
            holder.daysLeft.setTextColor(ContextCompat.getColor(context!!,R.color.colorExpired))

        }else if(daysBetween==null){
           holder.daysLeft.text=""
        }else{
            if(daysBetween<=3){
                holder.daysLeft.setTextColor(ContextCompat.getColor(context!!,R.color.colorGoingToExpire))
            }else{
                holder.daysLeft.setTextColor(ContextCompat.getColor(context!!,R.color.colorFresh))
            }
            holder.daysLeft.text  = "$daysBetween days"
        }

        holder.quanity.text = current.quantity.toString()
    }

}