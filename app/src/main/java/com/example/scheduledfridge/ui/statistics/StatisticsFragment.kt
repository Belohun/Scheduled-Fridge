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
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_statistics.*


class StatisticsFragment : Fragment() {
    private val statisticsViewModel: StatisticsViewModel by activityViewModels()
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
            setUpPieChart(it)

        })
    }

    private fun setUpPieChart(it: List<Statistic>) {
        var eatenCount = 0.00
        it.forEach { statistic ->
            if (statistic.eaten) {
                eatenCount++
            }
        }
        val thrownAwayCount = it.count() - eatenCount
        val eatenStatistics = ArrayList<PieEntry>()
        eatenStatistics.add(PieEntry(eatenCount.toFloat(), getString(R.string.eaten)))
        eatenStatistics.add(PieEntry(thrownAwayCount.toFloat(), getString(R.string.thrownAway)))
        val pieDataSet = PieDataSet(eatenStatistics, "")
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
        val l: Legend = statistics_PieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.textSize = 14F
        l.textColor = requireContext().getColor(R.color.colorText)
        l.setDrawInside(false)
        statistics_PieChart.data = pieData
        statistics_PieChart.setUsePercentValues(true)
        statistics_PieChart.setDrawEntryLabels(false)
        statistics_PieChart.isDrawHoleEnabled = true
        statistics_PieChart.centerText =centerText
        statistics_PieChart.setCenterTextSize(28f)
        statistics_PieChart.setCenterTextColor(requireContext().getColor(R.color.colorText))
        statistics_PieChart.setHoleColor(requireContext().getColor(R.color.colorBackground))
        statistics_PieChart.invalidate()
        statistics_PieChart.description.isEnabled = false
        statistics_PieChart.animate()
    }
}