package com.example.scheduledfridge.ui.home

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.scheduledfridge.R
import com.example.scheduledfridge.database.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
        val productName: TextView = root.findViewById(R.id.product_name_textView)
        val productGroup: TextView = root.findViewById(R.id.product_group_textView)
        val productValidity: TextView = root.findViewById(R.id.validity_textView)
        var _allProducts : List<Product> = emptyList()
        homeViewModel.allProducts.observe(viewLifecycleOwner, Observer {
            Log.d("products",it.toString())
            _allProducts= it
        })
        homeViewModel.productName.observe(viewLifecycleOwner, Observer {
            productName.text = it
        })
        homeViewModel.productGroup.observe(viewLifecycleOwner, Observer {
            productGroup.text = it
        })
        homeViewModel.productValidity.observe(viewLifecycleOwner, Observer {
            productValidity.text = it
        })
        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab.setOnClickListener {
            val dialogView = LayoutInflater.from(this.activity).inflate(R.layout.add_product_layout,null)
            val mBuilder = AlertDialog.Builder(this.activity!!)
                .setView(dialogView)
            val mAlertDialog = mBuilder.show()
            val typesOfFood = arrayOf("Item 1", "Item 2", "Item 3", "Item 4")
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(context!!, R.layout.support_simple_spinner_dropdown_item,typesOfFood)
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
                val id = _allProducts.last().id + 1
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
}