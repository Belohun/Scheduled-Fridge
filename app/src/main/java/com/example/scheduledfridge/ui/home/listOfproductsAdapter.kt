package com.example.scheduledfridge.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.scheduledfridge.R
import com.example.scheduledfridge.database.Product
import com.example.scheduledfridge.functions.productViewFunctions
import com.example.scheduledfridge.ui.productDetails.ProductDetailsFragment

import com.example.scheduledfridge.ui.productDetails.ProductDetailsViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class listOfproductsAdapter internal constructor(context: Context?): RecyclerView.Adapter<listOfproductsAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var products = emptyList<Product>()
    val context = context
    var navController: NavController?=null
     val currentProduct = MutableLiveData<Product>()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val productName = itemView.findViewById<TextView>(R.id.product_name_textView)
        val daysLeft = itemView.findViewById<TextView>(R.id.daysLeft_textView)
        val quanity = itemView.findViewById<TextView>(R.id.product_quanity_textView)
        val icon = itemView.findViewById<ImageView>(R.id.type_Image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        navController= Navigation.findNavController(parent)

        val itemView = inflater.inflate(R.layout.product_layout,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return products.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.animation = AnimationUtils.loadAnimation(context,R.anim.fade_scale)
        val current = products[position]

        holder.productName.text = current.productName
        holder.quanity.text = current.quantity.toString()
        productViewFunctions().setImage(holder.icon,current,context!!)
        productViewFunctions().setdaysBetween(holder.daysLeft,context,current)

        holder.itemView.setOnClickListener {
            navController!!.navigate(R.id.action_nav_home_to_nav_productDetails)
            setCurrentProduct(current)



        }

    }

    internal fun setProducts(products: List<Product>){
        this.products = products
        notifyDataSetChanged()
    }
/*    internal fun getCurrentProduct(): Product? {
     return _currentProduct.value
    }*/
    internal fun setCurrentProduct(product : Product){
        currentProduct.value = product

    }
}

