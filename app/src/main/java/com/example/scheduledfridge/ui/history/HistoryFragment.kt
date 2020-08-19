package com.example.scheduledfridge.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledfridge.R
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {
    private val  historyViewModel: HistoryViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().appBar_layout.elevation = 8F
        val historyAdapter = HistoryAdapter(context)
        history_RecyclerView.adapter = historyAdapter
        history_RecyclerView.layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL,false)
        historyViewModel.allHistory.observe(viewLifecycleOwner, Observer {
           historyAdapter.setHistory(it)
        })
    }
}