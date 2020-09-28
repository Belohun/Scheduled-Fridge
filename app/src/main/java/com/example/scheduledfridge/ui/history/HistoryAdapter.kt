package com.example.scheduledfridge.ui.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledfridge.R
import com.example.scheduledfridge.database.History
import com.example.scheduledfridge.utils.ViewUtils
import kotlinx.android.synthetic.main.product_history_layout.view.*


class HistoryAdapter internal constructor(val context: Context?): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private var history = ArrayList<History>()
    private val inflater: LayoutInflater = LayoutInflater.from(context)


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val date = itemView.product_history_date!!
        val typeImage = itemView.product_history_type_image!!
        val name = itemView.product_history_name!!
        val actionImage = itemView.product_history_action_image!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.product_history_layout,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
      return history.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = history[position]
        holder.date.text = current.date
        holder.name.text = current.name
        ViewUtils().setImage(holder.typeImage,current.type,context!!)
        setActionIcon(current.action,holder.actionImage)

    }
    private fun setActionIcon(action: String,imageView: ImageView){
        when(action){
            "Added" ->{
                imageView.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_baseline_add_circle_outline_24))
            }
            "Eaten"->{
                imageView.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_check_circle))
            }
            "ThrownAway"->{
                imageView.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_delete))
            }
            "Updated"->{
                imageView.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_baseline_edit_24))
            }
        }
    }
    fun setHistory(historyList: List<History>){
        history.removeAll(history)
        history.addAll(historyList)
        notifyDataSetChanged()

    }

}
