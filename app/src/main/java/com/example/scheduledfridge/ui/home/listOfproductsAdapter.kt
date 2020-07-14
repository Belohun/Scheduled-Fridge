package com.example.scheduledfridge.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
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
import kotlin.collections.ArrayList
import kotlin.coroutines.coroutineContext

class listOfproductsAdapter internal constructor(context: Context?): RecyclerView.Adapter<listOfproductsAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var products = emptyList<Product>()
    val context = context

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val productName = itemView.findViewById<TextView>(R.id.product_name_textView)
        val daysLeft = itemView.findViewById<TextView>(R.id.daysLeft_textView)
        val quanity = itemView.findViewById<TextView>(R.id.product_quanity_textView)
        val icon = itemView.findViewById<ImageView>(R.id.type_Image)

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
        val daysBetween: Long
        if(current.productExpirationDate==""){
                holder.daysLeft.text=""
        }else{
            val toDate = LocalDate.parse(current.productExpirationDate,formatter)
            daysBetween = ChronoUnit.DAYS.between(today,toDate)
            if(daysBetween<=0){
                holder.daysLeft.text="Expired"
                holder.daysLeft.setTextColor(ContextCompat.getColor(context!!,R.color.colorExpired))

            }else  {
                if(daysBetween<=3){
                    holder.daysLeft.setTextColor(ContextCompat.getColor(context!!,R.color.colorGoingToExpire))
                }else{
                    holder.daysLeft.setTextColor(ContextCompat.getColor(context!!,R.color.colorFresh))
                }
                holder.daysLeft.text  = "$daysBetween days"
            }
        }
        holder.productName.text = current.productName


        holder.quanity.text = current.quantity.toString()
    when(current.productType){
        "Vegetables"->{
            holder.icon.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_vegetables))
        }
        "Fruits"->{
            holder.icon.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_fruits))
        }
        "Sweets"->{
            holder.icon.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_sweets))
        }
        "Animal origin"->{
            holder.icon.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_animal_origin))
        }
        "Grain products"->{
            holder.icon.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_grain_products))
        }
        "Drinks"->{
            holder.icon.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_drinks))
        }
        "Spices"->{
            holder.icon.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_spices))
        }
        "Others"->{
            holder.icon.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_others))
        }
    }
    }
    internal fun setProducts(products: List<Product>){
        this.products = products
        notifyDataSetChanged()


    }




}