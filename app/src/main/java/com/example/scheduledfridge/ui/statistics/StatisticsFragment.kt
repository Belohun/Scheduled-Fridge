package com.example.scheduledfridge.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.scheduledfridge.R
import com.example.scheduledfridge.database.Statistic
import com.example.scheduledfridge.ui.history.HistoryViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_statistics.*


class StatisticsFragment : Fragment() {
    private val statisticsViewModel: StatisticsViewModel by activityViewModels()
    private val  historyViewModel: HistoryViewModel by activityViewModels()
    private val historyTypesAddedProducts = ArrayList<String>()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().appBar_layout.elevation = 8F
        super.onViewCreated(view, savedInstanceState)
        statisticsViewModel.allStatistic.observe(viewLifecycleOwner, Observer {
            setUpDeletedPieChart(it)
        })
        historyViewModel.allHistory.observe(viewLifecycleOwner, Observer { it ->
            it.forEach { historyProduct ->
                if(historyProduct.action=="Added"){
                    historyTypesAddedProducts.add(historyProduct.type)
                }
            }

            setUpAddedPieChart()

        })




    }

    private fun setUpAddedPieChart() {
        val addedStatistics = ArrayList<PieEntry>()
        val typesList = resources.getStringArray(R.array.types)
        typesList.forEach {
            addedStatistics.add(PieEntry(countStringInHistoryTypes(it).toFloat(), it))

        }

        val barDataSet = PieDataSet(addedStatistics, "")
        val colors = resources.getIntArray(R.array.rainbow).toMutableList()
        barDataSet.colors = colors
        barDataSet.setDrawValues(false)
        val barData = PieData(barDataSet)
        val centerText = addedStatistics.count()
            .toString() + " " + requireContext().getString(R.string.AddedProducts)
        statistics_addedProducts_PieChart.data = barData
        statistics_addedProducts_PieChart.invalidate()
        statistics_addedProducts_PieChart.description.isEnabled = false
        statistics_addedProducts_PieChart.setDrawEntryLabels(false)
        statistics_addedProducts_PieChart.centerText = centerText
        statistics_addedProducts_PieChart.setHoleColor(requireContext().getColor(R.color.colorBackground))
        statistics_addedProducts_PieChart.setCenterTextColor(requireContext().getColor(R.color.colorText))
        statistics_addedProducts_PieChart.isDrawHoleEnabled = true
        val l: Legend = statistics_addedProducts_PieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.isWordWrapEnabled = true
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.textSize = 14F
        l.textColor = requireContext().getColor(R.color.colorText)
        l.setDrawInside(false)
    }


    private fun countStringInHistoryTypes(string: String): Int {
    return historyTypesAddedProducts.count{item -> item == string}


}

    private fun setUpDeletedPieChart(it: List<Statistic>) {
        val eatenCount = it.count { statistic -> statistic.eaten  }
        val thrownAwayCount = it.count() - eatenCount
        val eatenStatistics = ArrayList<PieEntry>()
        eatenStatistics.add(PieEntry(eatenCount.toFloat(), getString(R.string.eaten)))
        eatenStatistics.add(PieEntry(thrownAwayCount.toFloat(), getString(R.string.thrownAway)))
        val pieDataSet = PieDataSet(eatenStatistics, "")
        pieDataSet.setDrawValues(false)
        pieDataSet.setColors(
            intArrayOf(
                R.color.colorFresh,
                R.color.colorExpired
            ), context
        )

        val pieData = PieData(pieDataSet)
        val centerText = it.count().toString() + " " + getString(R.string.deletedProducts)
        pieData.isHighlightEnabled = false
        pieData.setValueTextColor(requireContext().getColor(R.color.colorText))
        pieData.setValueTextSize(24f)
        val l: Legend = statistics_deletedProducts_PieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.textSize = 14F
        l.textColor = requireContext().getColor(R.color.colorText)
        l.setDrawInside(false)
        statistics_deletedProducts_PieChart.data = pieData
        statistics_deletedProducts_PieChart.setUsePercentValues(true)
        statistics_deletedProducts_PieChart.setDrawEntryLabels(false)
        statistics_deletedProducts_PieChart.isDrawHoleEnabled = true
        statistics_deletedProducts_PieChart.centerText =centerText
        statistics_deletedProducts_PieChart.setCenterTextSize(28f)
        statistics_deletedProducts_PieChart.setCenterTextColor(requireContext().getColor(R.color.colorText))
        statistics_deletedProducts_PieChart.setHoleColor(requireContext().getColor(R.color.colorBackground))
        statistics_deletedProducts_PieChart.invalidate()
        statistics_deletedProducts_PieChart.description.isEnabled = false
        statistics_deletedProducts_PieChart.animate()
    }
}