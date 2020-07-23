package com.example.scheduledfridge.ui.home
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledfridge.R
import kotlinx.android.synthetic.main.category_layout.view.*


class CategoriesAdapter internal constructor(val context: Context?,
                                             private var categories: ArrayList<String>
): RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val categoryName = itemView.categoryName_TextView!!

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesAdapter.ViewHolder {

        val itemView = inflater.inflate(R.layout.category_layout,parent,false)
        return ViewHolder(itemView)

    }

    override fun getItemCount(): Int {
       return categories.size
    }

    override fun onBindViewHolder(holder: CategoriesAdapter.ViewHolder, position: Int) {


      holder.categoryName.text = categories[position]
    }

}