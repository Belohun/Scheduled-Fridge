package com.example.scheduledfridge.functions

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.scheduledfridge.R
import com.example.scheduledfridge.database.Product
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class productViewFunctions {
   fun setImage(imageView: ImageView,product: Product,context: Context) {
       when(product.productType){
           "Vegetables"->{
               imageView.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_vegetables))
           }
           "Fruits"->{
               imageView.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_fruits))
           }
           "Sweets"->{
              imageView.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_sweets))
           }
           "Animal origin"->{
               imageView.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_animal_origin))
           }
           "Grain products"->{
               imageView.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_grain_products))
           }
           "Drinks"->{
               imageView.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_drinks))
           }
           "Spices"->{
               imageView.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_spices))
           }
           "Others"->{
               imageView.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_others))
           }
       }


   }

    @SuppressLint("NewApi")
    fun setdaysBetween(textView: TextView, context: Context, product: Product) {
        var formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
        val today= LocalDate.now()
        val daysBetween: Long
        if(product.productExpirationDate==""){
            textView.text=""
        }else{
            val toDate = LocalDate.parse(product.productExpirationDate,formatter)
            daysBetween = ChronoUnit.DAYS.between(today,toDate)
            if(daysBetween<=0){
                textView.text="Expired"
               textView.setTextColor(ContextCompat.getColor(context!!,R.color.colorExpired))

            }else  {
                if(daysBetween<=3){
                    textView.setTextColor(ContextCompat.getColor(context!!,R.color.colorGoingToExpire))
                }else{
                    textView.setTextColor(ContextCompat.getColor(context!!,R.color.colorFresh))
                }
                textView.text= "$daysBetween days"
            }
        }

    }


}