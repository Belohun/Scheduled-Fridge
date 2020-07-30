package com.example.scheduledfridge.utils
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

fun returnIconDrawable(type: String,context: Context): Bitmap? {
    return  when (type) {
        "Vegetables" -> {
            return BitmapFactory.decodeResource(
                context.resources,
                R.drawable.notification_vegetables
            )
        }
        "Fruits" -> {
            return BitmapFactory.decodeResource(
                context.resources,
                R.drawable.notification_fruits
            )
        }
        "Sweets" -> {
            return BitmapFactory.decodeResource(
                context.resources,
                R.drawable.notification_sweets
            )
        }
        "Animal origin" -> {
            return BitmapFactory.decodeResource(
                context.resources,
                R.drawable.notification_animal_origin
            )
        }
        "Grain products" -> {
            return BitmapFactory.decodeResource(
                context.resources,
                R.drawable.notification_grain_products
            )
        }
        "Drinks" -> {
            return BitmapFactory.decodeResource(
                context.resources,
                R.drawable.notification_drink
            )
        }
        "Spices" -> {
            return BitmapFactory.decodeResource(
                context.resources,
                R.drawable.notification_spieces
            )
        }
        "Meat" -> {
            return BitmapFactory.decodeResource(
                context.resources,
                R.drawable.notification_meat
            )
        }
        else -> {
                return BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.notification_others
                )
        }

    }
}


    @RequiresApi(Build.VERSION_CODES.O)
    fun setDaysBetween(textView: TextView, context: Context, product: Product) {
        val daysLeft: Long
        val texViewText: String
        if(product.productExpirationDate==""){
            texViewText=""
        }else{

            daysLeft = getDaysLeft(product.productExpirationDate,context)
            if(daysLeft<=0){
                texViewText = context.getString(R.string.Expired)

               textView.setTextColor(ContextCompat.getColor(context,R.color.colorExpired))
            }else  {
                if(daysLeft<=3){
                    textView.setTextColor(ContextCompat.getColor(context,R.color.colorGoingToExpire))
                }else{
                    textView.setTextColor(ContextCompat.getColor(context,R.color.colorFresh))
                }
                 texViewText = daysLeft.toString()+ " " + context.getString(R.string.Days)
            }
        }
        textView.text= texViewText

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDaysLeft(expirationDate: String, context: Context): Long {
        val formatter =
            DateTimeFormatter.ofPattern(context.getString(R.string.datePattern))
        val toDate = LocalDate.parse(expirationDate, formatter)
        val today = LocalDate.now()
        return ChronoUnit.DAYS.between(today, toDate)
    }



}