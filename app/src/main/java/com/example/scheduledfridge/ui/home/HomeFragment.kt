package com.example.scheduledfridge.ui.home
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledfridge.R
import com.example.scheduledfridge.database.Product
import com.example.scheduledfridge.ui.productDetails.ProductDetailsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.add_product_layout.*
import kotlinx.android.synthetic.main.add_product_layout.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(),MenuItem.OnActionExpandListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private  val homeViewModel: HomeViewModel by activityViewModels()
    private val productDetailsViewModel: ProductDetailsViewModel by activityViewModels()
    private var listOfProductsAdapter: ListOfProductsAdapter? = null
    private var categoriesAdapter: CategoriesAdapter? = null
    var currentProducts : List<Product> = emptyList()
    private var allProducts : List<Product> = emptyList()

    @SuppressLint("NewApi")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        setHasOptionsMenu(true)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val categoryArrayAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.types,
            R.layout.support_simple_spinner_dropdown_item
        )
        listOfProductsAdapter = ListOfProductsAdapter(context)
        categoriesAdapter = CategoriesAdapter(context)
        recyclerView_Categories.adapter =categoriesAdapter



        homeViewModel.allProducts.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            currentProducts=it
            allProducts=it
            val categories = returnPresentCategories(allProducts)
            if (categories.size == 1){
                categoriesAdapter!!.setCategories(emptyList())
            }else {
                categoriesAdapter!!.setCategories(categories)
            }
                listOfProductsAdapter!!.setProducts(currentProducts)
        })

        categoriesAdapter!!.currentCategories.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it!=null){
                val products = categorizeProducts(allProducts,it)

                currentProducts = products
                listOfProductsAdapter!!.setProducts(products)
                val isScrollable =isScrollable(nestedScrollView_home)
                if(!isScrollable){
                    fab.show()
                }
            }
        })
        listOfProductsAdapter!!.currentProduct.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            productDetailsViewModel.setCurrentProduct(it)
        })

        recyclerView_home.adapter = listOfProductsAdapter
        val isScrollable =isScrollable(nestedScrollView_home)
        if(!isScrollable){
            Log.d("isScrollable",isScrollable.toString())
            fab.show()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nestedScrollView_home.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (scrollY > oldScrollY) {
                    fab.hide()
                }
                if (scrollY < oldScrollY) {
                    fab.show()
                }
                if (scrollY == 0) {
                   fab.show()
                }
              /*  if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                   fab.show()
                }*/


            })}





        recyclerView_home.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        recyclerView_Categories.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        val simpleCallBackHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or  RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when(direction){
                    ItemTouchHelper.LEFT ->{
                        homeViewModel.delete(currentProducts[position])
                    }
                    RIGHT ->{
                        homeViewModel.delete(currentProducts[position])
                    }

                }

            }
            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftActionIcon(R.drawable.ic_check_circle)
                    .addSwipeRightActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate()
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallBackHelper)
        itemTouchHelper.attachToRecyclerView(recyclerView_home)
        fabOnClick(fab,categoryArrayAdapter)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun isScrollable(scrollView: NestedScrollView): Boolean {
        val childHeight = scrollView.height
        return scrollView.height < childHeight + scrollView.paddingTop + scrollView.paddingBottom
    }

    private fun fabOnClick(fab: FloatingActionButton, adapter: ArrayAdapter<CharSequence>) {
        fab.setOnClickListener {
            val dialogView =
                LayoutInflater.from(this.activity).inflate(R.layout.add_product_layout, null)
            val mBuilder = AlertDialog.Builder(this.requireActivity())
                .setView(dialogView)
            val mAlertDialog = mBuilder.show()

1

            mAlertDialog.type_AutoCompleteTextView.setAdapter(adapter)
            calendarOnClick(dialogView, mAlertDialog)

            buttonAddOnClick(dialogView, mAlertDialog)
            mAlertDialog.type_AutoCompleteTextView.setOnClickListener{
                mAlertDialog.type_AutoCompleteTextView.error = null
            }

            dialogView.btn_cancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }

    private fun calendarOnClick(
        dialogView: View,
        mAlertDialog: AlertDialog
    ) {
        dialogView.calendar_ImageButton.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                this.requireActivity(),
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    val date = "" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year
                    mAlertDialog.date_TextInputEditText.setText(date)
                },
                year,
                month,
                day
            )
            dpd.show()
        }
    }

    @SuppressLint("NewApi")
    private fun buttonAddOnClick(
        dialogView: View,
        mAlertDialog: AlertDialog
    ) {
        dialogView.btn_add.setOnClickListener {

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val formatted = current.format(formatter)
            var noErrors = true
            when {
                dialogView.add_product_name_text.text!!.length > 26 -> {
                    noErrors=false
                    dialogView.add_product_name_text.error="Name should be max length of 26 characters!"
                }
                dialogView.add_product_name_text.text!!.isEmpty() -> {
                    noErrors=false
                    dialogView.add_product_name_text.error="Field must not be empty!"
                }
                else -> {
                    dialogView.add_product_name_text.error = null
                }
            }
            when {
                dialogView.quantity_TextInputEditText.text!!.isEmpty() -> {
                    noErrors=false
                    dialogView.quantity_TextInputEditText.error="Field must not be empty!"

                }
                dialogView.quantity_TextInputEditText.text!!.length > 5 -> {
                    noErrors=false
                    dialogView.quantity_TextInputEditText.error="Name should be max length of 5 characters!"
                }
                else -> {
                    dialogView.quantity_TextInputEditText.error = null
                }
            }
            if( dialogView.type_AutoCompleteTextView!!.text.isEmpty()){
                noErrors=false
                dialogView.type_AutoCompleteTextView.error ="Select one of types!"
            }
            else {
                dialogView.type_AutoCompleteTextView.error = null
            }

            val id: Int = if (allProducts.isEmpty()) {
                1
            } else {
                allProducts.last().id + 1
            }


            if(noErrors){
                val product = Product(
                    id,
                    mAlertDialog.add_product_name_text.text.toString(),
                    mAlertDialog.type_AutoCompleteTextView.text.toString(),
                    mAlertDialog.date_TextInputEditText.text.toString(),
                    formatted.toString()
                    ,
                    mAlertDialog.quantity_TextInputEditText.text.toString().toInt()
                )

                homeViewModel.insert(product)
                mAlertDialog.dismiss()
            }

            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        val searchView: androidx.appcompat.widget.SearchView = menu.findItem(R.id.action_search).actionView as androidx.appcompat.widget.SearchView
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = requireContext().getString(R.string.Search___)
        super.onCreateOptionsMenu(menu, inflater)

    }
    override fun onQueryTextSubmit(p0: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextChange(p0: String?): Boolean {
    if(p0 == null || p0.trim().isEmpty()){
        listOfProductsAdapter!!.setProducts(currentProducts)
        return false
    }
        val newText = p0.toLowerCase(Locale.ROOT)
        val filteredNewsList: ArrayList<Product> = ArrayList()
        currentProducts.forEach{
            val name = it.productName.toLowerCase(Locale.ROOT)
            if(name.contains(newText) or it.quantity.toString().contains(newText)){
                filteredNewsList.add(it)
            }
        }
        listOfProductsAdapter!!.setProducts(filteredNewsList)
        return true
    }
    override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }
    override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
        listOfProductsAdapter!!.setProducts(currentProducts)
        return true
    }
    private fun categorizeProducts(allProducts: List<Product>,currentCategories: List<String>): ArrayList<Product> {
         val tempProducts: ArrayList<Product> = ArrayList()
        allProducts.forEach{
            if(currentCategories.contains(it.productType)){
                tempProducts.add(it)
            }
        }
        return tempProducts

    }
    private fun returnPresentCategories(allProducts: List<Product>): ArrayList<String> {
        val categories: ArrayList<String> = ArrayList()
        allProducts.forEach {
            if(!categories.contains(it.productType)){
                categories.add(it.productType)
            }
        }
        return categories

    }

}