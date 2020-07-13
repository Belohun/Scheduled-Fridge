package com.example.scheduledfridge.ui.home

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledfridge.R
import com.example.scheduledfridge.database.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.add_product_layout.*
import kotlinx.android.synthetic.main.add_product_layout.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    @SuppressLint("NewApi")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        var _allProducts : List<Product> = emptyList()

        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView_home)
        homeViewModel.allProducts.observe(viewLifecycleOwner, Observer {
            Log.d("products",it.toString())
            _allProducts= it
            val adapter = listOfproductsAdapter(context,_allProducts)
            setAdapter(recyclerView,adapter)
        })


        recyclerView.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
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
                        homeViewModel.delete(_allProducts[position])
                    }
                    RIGHT ->{
                        homeViewModel.delete(_allProducts[position])
                    }

                }

            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftActionIcon(R.drawable.ic_check_circle)
                    .addSwipeRightActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate()

            }








        }
        val itemTouchHelper = ItemTouchHelper(simpleCallBackHelper)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab.setOnClickListener {
            val dialogView = LayoutInflater.from(this.activity).inflate(R.layout.add_product_layout,null)
            val mBuilder = AlertDialog.Builder(this.activity!!)
                .setView(dialogView)
            val mAlertDialog = mBuilder.show()
            val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(context!!,R.array.types,R.layout.support_simple_spinner_dropdown_item)
            mAlertDialog.type_AutoCompliteTextView.setAdapter(adapter)



            dialogView.calendar_ImageButton.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val dpd = DatePickerDialog(this.activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    mAlertDialog.date_TextInputEditText.setText("" + dayOfMonth  +"/" + (monthOfYear + 1) + "/" + year)
                }, year, month, day)
                dpd.show()
            }
            dialogView.btn_add.setOnClickListener {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val formatted = current.format(formatter)
                //val id = homeViewModel.getLowestPossibleId(_allProducts)
                val id: Int
                if(_allProducts.isEmpty()){
                   id = 1
                }else{
                    id = _allProducts.last().id + 1
                }
                val _product = Product(id,mAlertDialog.add_product_name_text.text.toString(),mAlertDialog.type_AutoCompliteTextView.text.toString(),mAlertDialog.date_TextInputEditText.text.toString(),formatted.toString()/*formatted.toString()*/,mAlertDialog.quanity_TextInputEditText.text.toString().toInt())
                homeViewModel.insert(_product)


                mAlertDialog.dismiss()
            }
            dialogView.btn_cancel.setOnClickListener{
                mAlertDialog.dismiss()
            }
        }
        return root
    }
    fun setAdapter(
        recyclerView: RecyclerView,
        adapter: listOfproductsAdapter
    ){

        recyclerView.adapter = adapter

    }
}