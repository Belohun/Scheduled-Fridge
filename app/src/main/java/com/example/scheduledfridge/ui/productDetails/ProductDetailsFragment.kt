package com.example.scheduledfridge.ui.productDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.scheduledfridge.R

private lateinit var productDetailsViewModel: ProductDetailsViewModel
class ProductDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_product_details, container, false)
        val nameTextView:TextView = root.findViewById(R.id.productName_TextView_product_details)
        val typeTextView:TextView = root.findViewById(R.id.type_textView_product_details)
        val quanityTextView:TextView = root.findViewById(R.id.quanity_textView_product_details)
        val expirationDateTextView:TextView = root.findViewById(R.id.expirationDate_textView_product_details)
        val addedDateTextView:TextView = root.findViewById(R.id.addedDate_textView_product_details)
        productDetailsViewModel.currentProduct.observe(viewLifecycleOwner, Observer {
            nameTextView.text = it.productName
            typeTextView.text = it.productType
            quanityTextView.text = it.quantity.toString()
            expirationDateTextView.text = it.productExpirationDate
            addedDateTextView.text = it.productAdedDate
        })
        return root
    }
}