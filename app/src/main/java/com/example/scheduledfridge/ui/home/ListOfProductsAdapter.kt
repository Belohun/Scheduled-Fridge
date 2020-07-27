package com.example.scheduledfridge.ui.home
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledfridge.R
import com.example.scheduledfridge.database.Product
import com.example.scheduledfridge.functions.ProductViewFunctions
import kotlinx.android.synthetic.main.product_layout.view.*


class ListOfProductsAdapter internal constructor(val context: Context?): RecyclerView.Adapter<ListOfProductsAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var products = emptyList<Product>()
    private var navController: NavController?=null
     val currentProduct = MutableLiveData<Product>()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val productName = itemView.product_name_textView!!
        val daysLeft = itemView.daysLeft_textView!!
        val quantity = itemView.product_quantity_textView!!
        val icon = itemView.type_Image!!

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
        val startOffsetMultiplier = 15
        animation.startOffset = (position*startOffsetMultiplier).toLong()
        holder.itemView.animation = animation
        val current = products[position]
        holder.productName.text = current.productName
        holder.quantity.text = current.quantity.toString()
        ProductViewFunctions().setImage(holder.icon,current,context!!)
        ProductViewFunctions().setDaysBetween(holder.daysLeft,context,current)
        holderOnClick(holder, current)

    }

    private fun holderOnClick(holder: ViewHolder, current: Product) {
        holder.itemView.setOnClickListener {
            holder.icon.transitionName = current.id.toString()
            holder.daysLeft.transitionName = current.id.toString() + context!!.getString(R.string.daysLeft)
            val extras = FragmentNavigatorExtras(
                holder.icon to current.id.toString(),
                holder.daysLeft to current.id.toString() + context.getString(R.string.daysLeft)
            )
            navController!!.navigate(R.id.action_nav_home_to_nav_productDetails, null, null, extras)
            setCurrentProduct(current)


        }
    }

    internal fun setProducts(products: List<Product>){
        this.products = products
        notifyDataSetChanged()
    }
    private fun setCurrentProduct(product : Product){
        currentProduct.value = product

    }
}

