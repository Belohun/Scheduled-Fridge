package com.example.scheduledfridge.ui.productDetails
import android.os.Build
import android.os.Bundle
import androidx.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.scheduledfridge.R
import com.example.scheduledfridge.utils.ViewUtils
import androidx.core.view.ViewCompat
import com.example.scheduledfridge.database.Product
import kotlinx.android.synthetic.main.fragment_product_details.*


class ProductDetailsFragment : Fragment() {
    private val productDetailsViewModel : ProductDetailsViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_product_details, container, false)
        val translationZ = 100f
        ViewCompat.setTranslationZ(root,translationZ)
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        productDetailsViewModel.currentProduct.observe(viewLifecycleOwner, Observer {
            setProductDetailsViews(it)
        })
        super.onViewCreated(view, savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setProductDetailsViews(it: Product) {
        if (it != null) {
            productName_TextView_product_details.text = it.productName
            type_textView_product_details.text = it.productType
            quantity_textView_product_details.text = it.quantity.toString()
            expirationDate_textView_product_details.text = it.productExpirationDate
            addedDate_textView_product_details.text = it.productAdedDate
            ViewUtils()
                .setImage(type_Image_product_details, it, requireContext())
            ViewUtils()
                .setDaysBetween(daysLeft_textView_product_details, requireContext(), it)
            type_Image_product_details.transitionName = it.id.toString()
            val daysLeftTransitionName = it.id.toString() + getString(R.string.daysLeft)
            daysLeft_textView_product_details.transitionName = daysLeftTransitionName
        }
    }
}