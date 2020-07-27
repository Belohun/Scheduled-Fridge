package com.example.scheduledfridge.ui.home
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledfridge.R
import kotlinx.android.synthetic.main.category_layout.view.*


class CategoriesAdapter internal constructor(val context: Context?): RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {
    private var categories: List<String> = emptyList()
    private var categoriesTemp= categories.toMutableList()
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    val currentCategories =  MutableLiveData<List<String>>()


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val categoryName = itemView.categoryName_TextView!!
        val layout = itemView.category_LinearLayout!!

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
        var isOn = true
        val current = categories[position]
        holder.layout.background = context!!.getDrawable(R.drawable.category_background)

      holder.categoryName.text = current

      holder.itemView.setOnClickListener {

              if (isOn){
                  isOn = false
                  categoriesTemp.remove(current)
                  holder.layout.background = context.getDrawable(R.drawable.category_background_off)


              }else {
                  categoriesTemp.add(current)
                  isOn = true
                  holder.layout.background = context.getDrawable(R.drawable.category_background)

              }
            setCurrentCategories(categoriesTemp)
          }



    }
    private fun setCurrentCategories(list: List<String>){
        currentCategories.value = list
    }
    fun setCategories(list: List<String>){
        categories = list
        categoriesTemp = list.toMutableList()
        notifyDataSetChanged()
    }

}