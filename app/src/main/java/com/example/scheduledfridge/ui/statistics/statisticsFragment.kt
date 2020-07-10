package com.example.scheduledfridge.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.scheduledfridge.R

class statisticsFragment : Fragment() {

    //private lateinit var statisticsViewModel: statisticsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val statisticsViewModel: statisticsViewModel by lazy {
            ViewModelProviders.of(this).get(statisticsViewModel::class.java)
        }
        val root = inflater.inflate(R.layout.fragment_statistics, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        statisticsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}