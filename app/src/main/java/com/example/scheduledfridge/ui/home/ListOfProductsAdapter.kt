package com.example.scheduledfridge.ui.home
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledfridge.R
import com.example.scheduledfridge.database.Product
import com.example.scheduledfridge.utils.ViewUtils
import kotlinx.android.synthetic.main.product_layout.view.*


class ListOfProductsAdapter internal constructor(val context: Context?, private val homeViewModel: HomeViewModel): RecyclerView.Adapter<ListOfProductsAdapter.ViewHolder>() {
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


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val current = products[position]
        var isSelected: Boolean
        var isSelectedMode:Boolean
        val animation = AnimationUtils.loadAnimation(context,R.anim.fade_scale)
        val startOffsetMultiplier = 15
        animation.startOffset = (position*startOffsetMultiplier).toLong()
        holder.itemView.animation = animation
        val selectedProducts = homeViewModel.getSelectedProducts()
        if(selectedProducts.contains(current)){
            Log.d("Contains",current.productName)
            holder.itemView.background = context!!.getDrawable(R.drawable.product_background_selected)
            isSelected= true
        }else{
            holder.itemView.background = context!!.getDrawable(R.drawable.product_background)
            isSelected= false
        }
        holder.productName.text = current.productName
        holder.quantity.text = current.quantity.toString()
        ViewUtils().setImage(holder.icon,current, context)
        ViewUtils().setDaysBetween(holder.daysLeft,context,current)
        holder.itemView.setOnClickListener {
            isSelectedMode = homeViewModel.getSelectingMode()
            if(isSelectedMode){
                val selectedProducts = homeViewModel.getSelectedProducts()
                if(isSelected){
                    isSelected = false
                    it.background = context.getDrawable(R.drawable.product_background)
                     selectedProducts.remove(current)

                }else{
                    isSelected=true
                    it.background = context.getDrawable(R.drawable.product_background_selected)
                    selectedProducts.add(current)
                }
                homeViewModel.setSelectedProducts(selectedProducts)
            }else{
                holder.icon.transitionName = current.id.toString()
                holder.daysLeft.transitionName = current.id.toString() + context.getString(R.string.daysLeft)
                val extras = FragmentNavigatorExtras(

                    holder.icon to current.id.toString(),
                    holder.daysLeft to current.id.toString() + context.getString(R.string.daysLeft)
                )
                navController!!.navigate(R.id.action_nav_home_to_nav_productDetails, null, null, extras)
                setCurrentProduct(current)
            }

        }

        holder.itemView.setOnLongClickListener {
            val selectedProducts = homeViewModel.getSelectedProducts()
            if(isSelected){
                isSelected = false
                it.background = context.getDrawable(R.drawable.product_background)
                selectedProducts.remove(current)
            }
            else {

                isSelectedMode = homeViewModel.getSelectingMode()
                it.background = context.getDrawable(R.drawable.product_background_selected)
                if(!homeViewModel.getSelectingMode()){
                    homeViewModel.setSelectingMode(true)

                }
                isSelected = true
                selectedProducts.add(current)
            }
            homeViewModel.setSelectedProducts(selectedProducts)
            true
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

