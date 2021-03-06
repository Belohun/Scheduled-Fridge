package com.example.scheduledfridge.ui.home
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledfridge.R
import com.example.scheduledfridge.database.Product
import com.example.scheduledfridge.utils.ViewUtils
import com.example.scheduledfridge.utils.cancelNotification
import com.example.scheduledfridge.utils.generateNotification
import kotlinx.android.synthetic.main.add_product_layout.*
import kotlinx.android.synthetic.main.product_layout.view.*


class ListOfProductsAdapter internal constructor(val context: Context?, private val homeViewModel: HomeViewModel): RecyclerView.Adapter<ListOfProductsAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var products =ArrayList<Product>()
    private var navController: NavController?=null
    private var currentPosition: Int? = null
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
        var isSelected : Boolean
        val animation = AnimationUtils.loadAnimation(context,R.anim.fade_scale)
        val startOffsetMultiplier = 15
        animation.startOffset = (position*startOffsetMultiplier).toLong()
        //holder.itemView.animation = animation
        val selectedProducts = homeViewModel.getSelectedProducts()
        isSelected = setUpProductView(selectedProducts, current, holder,  context)



        holder.itemView.setOnLongClickListener {
        val popUpMenu = PopupMenu(context,it)
        popUpMenu.inflate(R.menu.menu_floating_context)
        popUpMenu.setOnMenuItemClickListener {menuItem ->
            when(menuItem.itemId){
                R.id.context_menu_edit -> {
                    editProduct(context, current)
                }
                R.id.context_menu_select ->{
                  isSelected =  selectProduct(isSelected, holder, context!!, current)

                }


            }
            true
        }
        popUpMenu.show()
        currentPosition = position
        true
    }
        holder.itemView.setOnClickListener {
           isSelected = itemViewOnClick(isSelected, it, context!!, current, holder)

        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpProductView(
        selectedProducts: ArrayList<Product>,
        current: Product,
        holder: ViewHolder,

        context: Context?
    ): Boolean {
        val isSelected1: Boolean
        if (selectedProducts.contains(current)) {
            Log.d("Contains", current.productName)
            holder.itemView.background =
                ContextCompat.getDrawable(context!!,R.drawable.product_background_selected)
            isSelected1 = true
        } else {
            holder.itemView.background = ContextCompat.getDrawable(context!!,R.drawable.product_background)
            isSelected1 = false
        }
        holder.productName.text = current.productName
        holder.quantity.text = current.quantity.toString()
        ViewUtils().setImage(holder.icon, current.productType, context)
        ViewUtils().setDaysBetween(holder.daysLeft, context, current)
        return isSelected1
    }

    private fun itemViewOnClick(
        isSelected: Boolean,
        it: View,
        context: Context,
        current: Product,
        holder: ViewHolder
    ): Boolean {
        var isSelected1 = isSelected
        val isSelectedMode = homeViewModel.getSelectingMode()
        if (isSelectedMode) {
            val selectedProducts = homeViewModel.getSelectedProducts()
            if (isSelected1) {
                isSelected1 = false
                it.background = ContextCompat.getDrawable(context,R.drawable.product_background)
                selectedProducts.remove(current)

            } else {
                isSelected1 = true
                it.background = ContextCompat.getDrawable(context,R.drawable.product_background_selected)
                selectedProducts.add(current)
            }
            homeViewModel.setSelectedProducts(selectedProducts)
        } else {
            holder.icon.transitionName = current.id.toString()
            holder.daysLeft.transitionName =
                current.id.toString() + context.getString(R.string.daysLeft)
            val extras = FragmentNavigatorExtras(

                holder.icon to current.id.toString(),
                holder.daysLeft to current.id.toString() + context.getString(R.string.daysLeft)
            )
            navController!!.navigate(
                R.id.action_nav_home_to_nav_productDetails,
                null,
                null,
                extras
            )
            setCurrentProduct(current)
        }
        return isSelected1
    }

    private fun selectProduct(
        isSelected: Boolean,
        holder: ViewHolder,
        context: Context,
        current: Product
    ): Boolean {
        val isSelectedLocal: Boolean
        val selectedProducts = homeViewModel.getSelectedProducts()
        if (isSelected) {
            holder.itemView.background = ContextCompat.getDrawable(context,R.drawable.product_background)
            selectedProducts.remove(current)
            isSelectedLocal = false
        } else {

            holder.itemView.background = ContextCompat.getDrawable(context,R.drawable.product_background_selected)
            if (!homeViewModel.getSelectingMode()) {
                homeViewModel.setSelectingMode(true)
            }
            isSelectedLocal = true
            selectedProducts.add(current)
        }
        homeViewModel.setSelectedProducts(selectedProducts)
    return isSelectedLocal
    }

    private fun editProduct(
        context: Context?,
        current: Product
    ) {
        val typesArrayAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            context!!,
            R.array.types,
            R.layout.support_simple_spinner_dropdown_item
        )
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.add_product_layout, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.productName_editText.setText(current.productName)
        mAlertDialog.date_TextInputEditText.setText(current.productExpirationDate)
        mAlertDialog.type_AutoCompleteTextView.setText(current.productType)
        mAlertDialog.quantity_TextInputEditText.setText(current.quantity.toString())
        mAlertDialog.btn_add.text = context.getString(R.string.Edit)
        mAlertDialog.type_AutoCompleteTextView.setAdapter(typesArrayAdapter)
        mAlertDialog.btn_cancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
        ViewUtils().calendarOnClick(dialogView, mAlertDialog, context)

        mAlertDialog.btn_add.setOnClickListener {
            var noErrors = true
            noErrors = ViewUtils().isNoErrorsAddProduct(dialogView, noErrors, context)
            if (noErrors) {
                val product = Product(
                    current.id,
                    mAlertDialog.productName_editText.text.toString(),
                    mAlertDialog.type_AutoCompleteTextView.text.toString(),
                    mAlertDialog.date_TextInputEditText.text.toString(),
                    current.productAddedDate
                    ,
                    mAlertDialog.quantity_TextInputEditText.text.toString().toInt()
                )
                if (product.productExpirationDate != "") {
                    cancelNotification(context,product.id)
                    generateNotification(
                        product.id,
                        product.productExpirationDate,
                        product.productName,
                        product.productType,
                        context
                    )
                }
               if(homeViewModel.getSelectedProducts().contains(current)){
                   val tempProducts =ArrayList<Product>()
                   tempProducts.addAll(homeViewModel.getSelectedProducts())
                   tempProducts.remove(current)
                   tempProducts.add(product)
                   homeViewModel.setSelectedProducts(tempProducts)

               }
                homeViewModel.updateProduct(product)
                mAlertDialog.dismiss()
            }
        }
    }


    fun setProducts(_products: List<Product>){
        products.removeAll(products)
         products.addAll( _products)
        notifyDataSetChanged()
    }
    private fun setCurrentProduct(product : Product){
        currentProduct.value = product

    }


}

