package com.example.scheduledfridge.utils
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.scheduledfridge.R
import com.example.scheduledfridge.database.Product
import kotlinx.android.synthetic.main.add_product_layout.*
import kotlinx.android.synthetic.main.add_product_layout.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class ViewUtils {
   fun setImage(imageView: ImageView,type: String,context: Context) {

       when(type){
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
    when (type) {
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


    fun getDaysLeft(expirationDate: String, context: Context): Long {
        val formatter =
            DateTimeFormatter.ofPattern(context.getString(R.string.datePattern))
        val toDate = LocalDate.parse(expirationDate, formatter)
        val today = LocalDate.now()
        return ChronoUnit.DAYS.between(today, toDate)
    }


    fun calendarOnClick(
        dialogView: View,
        mAlertDialog: AlertDialog,
        context: Context
    ) {
        dialogView.calendar_ImageButton.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                context,
                DatePickerDialog.OnDateSetListener { _, _year, _monthOfYear, _dayOfMonth ->
                    val date = "" + _dayOfMonth + "/" + (_monthOfYear + 1) + "/" + _year
                    mAlertDialog.date_TextInputEditText.setText(date)
                },
                year,
                month,
                day
            )
            dpd.show()
        }
    }
     fun isNoErrorsAddProduct(dialogView: View, noErrors: Boolean, context: Context): Boolean {
        var noErrors1 = noErrors
        when {
            dialogView.productName_editText.text!!.length > 20 -> {
                noErrors1 = false
                dialogView.productName_editText.error =
                    context.getString(R.string.max20Char)
            }
            dialogView.productName_editText.text!!.isEmpty() -> {
                noErrors1 = false
                dialogView.productName_editText.error =
                    context.getString(R.string.fieldMustNotBeEmpty)
            }
            else -> {
                dialogView.productName_editText.error = null
            }
        }
        when {
            dialogView.quantity_TextInputEditText.text!!.isEmpty() -> {
                noErrors1 = false
                dialogView.quantity_TextInputEditText.error =
                    context.getString(R.string.fieldMustNotBeEmpty)

            }
            dialogView.quantity_TextInputEditText.text!!.length > 5 -> {
                noErrors1 = false
                dialogView.quantity_TextInputEditText.error =
                    context.getString(R.string.quantityMax5Char)
            }
            else -> {
                dialogView.quantity_TextInputEditText.error = null
            }
        }
        if (dialogView.type_AutoCompleteTextView!!.text.isEmpty()) {
            noErrors1 = false
            dialogView.type_AutoCompleteTextView.error =
                context.getString(R.string.selectOneOfTypes)
        } else {
            dialogView.type_AutoCompleteTextView.error = null
        }
        return noErrors1
    }
}