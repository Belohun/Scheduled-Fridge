package com.example.scheduledfridge.ui.home
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledfridge.R
import com.example.scheduledfridge.database.Product
import com.example.scheduledfridge.functions.productViewFunctions
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
        val animation = AnimationUtils.loadAnimation(context,R.anim.fade_scale)
        animation.startOffset = (position*30).toLong()
        holder.itemView.animation = animation
        val current = products[position]

        holder.productName.text = current.productName
        holder.quanity.text = current.quantity.toString()
        productViewFunctions().setImage(holder.icon,current,context!!)
        productViewFunctions().setdaysBetween(holder.daysLeft,context,current)

        holder.itemView.setOnClickListener {
            holder.icon.transitionName = current.id.toString()
            holder.daysLeft.transitionName = current.id.toString() + "daysLeft"
            val extras = FragmentNavigatorExtras(
                holder.icon to current.id.toString(),
                holder.daysLeft to current.id.toString() + "daysLeft"
                )
            navController!!.navigate(R.id.action_nav_home_to_nav_productDetails,null,null,extras)
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
    private fun setCurrentProduct(product : Product){
        currentProduct.value = product

    }
}

