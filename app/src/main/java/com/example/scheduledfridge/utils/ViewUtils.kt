package com.example.scheduledfridge.utils
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.scheduledfridge.R
import com.example.scheduledfridge.database.Product
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class ViewUtils {
   fun setImage(imageView: ImageView,product: Product,context: Context) {

       when(product.productType){
           "Vegetables"->{
               imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_vegetables))
           }
           "Fruits"->{
               imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fruits))
           }
           "Sweets"->{
              imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sweets))
           }
           "Animal origin"->{
               imageView.setImageDrawable(ContextCompat.getDrawable(context ,R.drawable.ic_animal_origin))
           }
           "Grain products"->{
               imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_grain_products))
           }
           "Drinks"->{
               imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_drinks))
           }
           "Spices"->{
               imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_spices))
           }
           "Meat"->{
               imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_meat))
           }

           "Others"->{
               imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_others))
           }
       }


   }

fun returnIconDrawable(product: Product,context: Context): Drawable? {
    return  when (product.productType) {
        "Vegetables" -> {
            return ContextCompat.getDrawable(context, R.drawable.ic_vegetables)
        }
        "Fruits" -> {
            return ContextCompat.getDrawable(context, R.drawable.ic_fruits)
        }
        "Sweets" -> {
            return ContextCompat.getDrawable(context, R.drawable.ic_sweets)
        }
        "Animal origin" -> {
            return ContextCompat.getDrawable(context, R.drawable.ic_animal_origin)
        }
        "Grain products" -> {
            return ContextCompat.getDrawable(context, R.drawable.ic_grain_products)
        }
        "Drinks" -> {
            return ContextCompat.getDrawable(context, R.drawable.ic_drinks)
        }
        "Spices" -> {
            return ContextCompat.getDrawable(context, R.drawable.ic_spices)
        }
        "Meat" -> {
            return ContextCompat.getDrawable(context, R.drawable.ic_meat)
        }

        else -> {
            return ContextCompat.getDrawable(context, R.drawable.ic_others)
        }

    }
}




    @SuppressLint("NewApi")
    fun setDaysBetween(textView: TextView, context: Context, product: Product) {
        val formatter = DateTimeFormatter.ofPattern(context.getString(R.string.datePattern))
        val today= LocalDate.now()
        val daysBetween: Long
        val texViewText: String
        if(product.productExpirationDate==""){
            texViewText=""
        }else{
            val toDate = LocalDate.parse(product.productExpirationDate,formatter)
            daysBetween = ChronoUnit.DAYS.between(today,toDate)
            if(daysBetween<=0){
                texViewText = context.getString(R.string.Expired)

               textView.setTextColor(ContextCompat.getColor(context,R.color.colorExpired))
            }else  {
                if(daysBetween<=3){
                    textView.setTextColor(ContextCompat.getColor(context,R.color.colorGoingToExpire))
                }else{
                    textView.setTextColor(ContextCompat.getColor(context,R.color.colorFresh))
                }
                 texViewText = daysBetween.toString()+ " " + context.getString(R.string.Days)
            }
        }
        textView.text= texViewText

    }


}