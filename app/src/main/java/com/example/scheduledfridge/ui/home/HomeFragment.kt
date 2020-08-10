package com.example.scheduledfridge.ui.home
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
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
import com.example.scheduledfridge.utils.Preferences
import com.example.scheduledfridge.utils.ViewUtils
import com.example.scheduledfridge.utils.cancelNotification
import com.example.scheduledfridge.utils.generateNotification
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.add_product_layout.*
import kotlinx.android.synthetic.main.add_product_layout.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.time.LocalDate
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
    private lateinit var  notificationManager: NotificationManager
    private var  sortByArrayAdapter: ArrayAdapter<CharSequence>? = null
    private var preferences: Preferences? = null
     var actionMode: ActionMode? = null
    private var actionModeCallback: ActionModeCallback? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        setHasOptionsMenu(true)
        createNotificationChannel(getString(R.string.notification_chanel_id),getString(R.string.notification_title))
            sortByArrayAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sortBy,
            R.layout.support_simple_spinner_dropdown_item
        )
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        actionModeCallback = ActionModeCallback()
        homeViewModel.setSelectingMode(false)
        preferences = Preferences(requireContext())
        autoCompleteView_sortBy.setAdapter(sortByArrayAdapter)
        val sortingOptionSaved = preferences!!.getSorting()
        autoCompleteView_sortBy.setText(sortingOptionSaved)

        autoCompleteView_sortBy.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, pos, _ ->
                val sortOptionsList = resources.getStringArray(R.array.sortBy)
                val current = sortOptionsList[pos]
                sortProducts(current)

            }
        val typesArrayAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.types,
            R.layout.support_simple_spinner_dropdown_item
        )



        notificationManager =
            ContextCompat.getSystemService(
                requireContext(),
                NotificationManager::class.java
            )!!

        listOfProductsAdapter = ListOfProductsAdapter(context,homeViewModel)

        categoriesAdapter = CategoriesAdapter(context)
        recyclerView_Categories.adapter =categoriesAdapter
        homeViewModel.isSelectingMode.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            actionMode = if(it==false){
                null
            }else {
                requireActivity().startActionMode(actionModeCallback)
            }
        })
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
            val isScrollable =isScrollable(nestedScrollView_home)
            if(!isScrollable){
                fab.show()
            }
            sortProducts(sortingOptionSaved)
        })

        categoriesAdapter!!.currentCategories.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it!=null){
                val products = categorizeProducts(allProducts,it)
                currentProducts = products
                listOfProductsAdapter!!.setProducts(products)
                val isScrollable =isScrollable(nestedScrollView_home)
                if(!isScrollable && actionMode==null){
                    fab.show()
                }
            }
        })
        listOfProductsAdapter!!.currentProduct.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            productDetailsViewModel.setCurrentProduct(it)
        })

        recyclerView_home.adapter = listOfProductsAdapter

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nestedScrollView_home.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY > oldScrollY) {
                    fab.hide()
                }
                if (scrollY < oldScrollY && actionMode==null) {
                    fab.show()
                }
                if (scrollY == 0 && actionMode==null) {
                   fab.show()
                }

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
                        deleteProduct(currentProducts[position])

                    }
                    RIGHT ->{
                       deleteProduct(currentProducts[position])
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
        fabOnClick(fab,typesArrayAdapter)
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


            mAlertDialog.type_AutoCompleteTextView.setAdapter(adapter)
            ViewUtils().calendarOnClick(dialogView, mAlertDialog,requireContext())

            buttonAddOnClick(dialogView, mAlertDialog)
            mAlertDialog.type_AutoCompleteTextView.setOnClickListener{
                mAlertDialog.type_AutoCompleteTextView.error = null
            }

            dialogView.btn_cancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }



    @SuppressLint("NewApi")
    private fun buttonAddOnClick(
        dialogView: View,
        mAlertDialog: AlertDialog
    ) {
        dialogView.btn_add.setOnClickListener {

            val current = LocalDateTime.now()
            val formatter =
                DateTimeFormatter.ofPattern(requireContext().getString(R.string.datePattern))
            val formatted = current.format(formatter)
            var noErrors = true
            noErrors = ViewUtils().isNoErrors(dialogView, noErrors,requireContext())
            val id: Int = if (allProducts.isEmpty()) {
                1
            } else {
                allProducts.maxBy { it.id }!!.id + 1
            }


            if (noErrors) {
                val product = Product(
                    id,
                    mAlertDialog.productName_editText.text.toString(),
                    mAlertDialog.type_AutoCompleteTextView.text.toString(),
                    mAlertDialog.date_TextInputEditText.text.toString(),
                    formatted.toString()
                    ,
                    mAlertDialog.quantity_TextInputEditText.text.toString().toInt()
                )
                if (product.productExpirationDate != "") {
                    generateNotification(product.id,product.productExpirationDate,product.productName,product.productType,requireContext())
                }
                homeViewModel.insertProduct(product)
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
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
    if(p0 == null || p0.trim().isEmpty()){
        currentProducts = allProducts
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
        currentProducts = filteredNewsList
        return true
    }
    override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
       return true
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
    private fun createNotificationChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )


            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED


            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
    @SuppressLint("NewApi")
    private fun sortProducts(sortingOption: String) {
        val sortOptionsList = resources.getStringArray(R.array.sortBy)
        val dateTimeFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern(requireContext().getString(R.string.datePattern))
        var tempList: List<Product> = emptyList()
        val tempListWithNullDates: ArrayList<Product> = ArrayList()
        val tempListWithoutNullDates: ArrayList<Product> = ArrayList()
        preferences = Preferences(requireContext())
        fun divideAllProducts() {
            allProducts.forEach {
                if (it.productExpirationDate == "") {
                    tempListWithNullDates.add(it)
                } else {
                    tempListWithoutNullDates.add(it)
                }
            }

        }
        when (sortingOption) {
            sortOptionsList[0] -> {
                tempList = allProducts.sortedByDescending {
                    LocalDate.parse(
                        it.productAdedDate,
                        dateTimeFormatter
                    )
                }.asReversed()

            }
            sortOptionsList[1] -> {
                tempList = allProducts.sortedByDescending {
                    LocalDate.parse(
                        it.productAdedDate,
                        dateTimeFormatter
                    )
                }

            }
            sortOptionsList[2] -> {
                divideAllProducts()
                val localTempList: ArrayList<Product> = ArrayList()
                localTempList.addAll(tempListWithoutNullDates.sortedByDescending {
                    LocalDate.parse(
                        it.productExpirationDate,
                        dateTimeFormatter
                    )
                }.asReversed())
                localTempList.addAll(tempListWithNullDates)
                tempList = localTempList
            }
            sortOptionsList[3] -> {
                divideAllProducts()
                val localTempList: ArrayList<Product> = ArrayList()
                localTempList.addAll(tempListWithNullDates)
                localTempList.addAll(tempListWithoutNullDates.sortedByDescending {
                    LocalDate.parse(
                        it.productExpirationDate,
                        dateTimeFormatter
                    )
                })
                tempList = localTempList
            }
            sortOptionsList[4] -> {
                tempList =
                    allProducts.sortedByDescending { it.productName }.asReversed()
            }
            sortOptionsList[5] -> {
                tempList = allProducts.sortedByDescending { it.productName }

            }
            sortOptionsList[6] -> {
                tempList = allProducts.sortedBy { it.productType }
            }

        }
        preferences!!.setSorting(sortingOption)
        allProducts = tempList
        currentProducts= tempList
        listOfProductsAdapter!!.setProducts(tempList)
    }

    override fun onStop() {
        super.onStop()
    requireActivity().appBar_layout.elevation = 8F
    }

    override fun onStart() {
        super.onStart()
        requireActivity().appBar_layout.elevation = 0F

    }

    override fun onResume() {
        super.onResume()
        sortByArrayAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sortBy,
            R.layout.support_simple_spinner_dropdown_item
        )
        autoCompleteView_sortBy.setAdapter(sortByArrayAdapter)
    }
     inner class  ActionModeCallback: ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode?, menuItem: MenuItem?): Boolean {
            when(menuItem!!.itemId){
                R.id.ate_menu_actionSelecting ->{
                    homeViewModel.getSelectedProducts().forEach{
                        deleteProduct(it)
                    }
                    mode!!.finish()
                }
                R.id.trashed_menu_actionSelecting ->{
                    homeViewModel.getSelectedProducts().forEach{
                        deleteProduct(it)
                    }
                    mode!!.finish()

                }
            }
           return false
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            fab.hide()
            mode!!.menuInflater.inflate(R.menu.menu_action_selecting , menu)
            mode.title = "Selected Products"
            return true
        }

        override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
        return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            fab.show()
            actionMode = null
            homeViewModel.setSelectingMode(false)
            homeViewModel.setSelectedProducts(ArrayList())
            listOfProductsAdapter!!.setProducts(allProducts)


        }
    }
    private fun deleteProduct(product: Product){
        homeViewModel.deleteProduct(product)
        cancelNotification(requireContext(),product.id)
    }

}