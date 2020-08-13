package com.example.scheduledfridge.ui.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledfridge.R
import com.example.scheduledfridge.utils.Preferences
import kotlinx.android.synthetic.main.settings_day_layout.view.*

class SettingsDaysBeforeExpiredAdapter internal constructor(val context: Context?): RecyclerView.Adapter<SettingsDaysBeforeExpiredAdapter.ViewHolder>() {
    private var days = ArrayList<String>()
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val preferences = Preferences(context!!)

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val day = itemView.settings_day_TextView!!
        val delete_button = itemView.settings_delete_day!!
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SettingsDaysBeforeExpiredAdapter.ViewHolder {
        val itemView = inflater.inflate(R.layout.settings_day_layout,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return days.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = days[position]
        holder.day.text = current
        holder.delete_button.setOnClickListener {
            preferences.deleteDaysBeforeExpiration(current)
            setDays(preferences.getDaysBeforeExpiration())
        }
    }
    fun setDays(newDays: ArrayList<String>){
        days= newDays
        notifyDataSetChanged()
    }
}