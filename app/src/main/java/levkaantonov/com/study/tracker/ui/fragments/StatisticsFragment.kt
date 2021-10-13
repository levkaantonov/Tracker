package levkaantonov.com.study.tracker.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import levkaantonov.com.study.tracker.R
import levkaantonov.com.study.tracker.databinding.FragmentStatisticsBinding
import levkaantonov.com.study.tracker.other.CustomMarkerView
import levkaantonov.com.study.tracker.other.TrackingUtility
import levkaantonov.com.study.tracker.ui.viewmodels.StatisticsViewModel
import java.lang.Math.round
import kotlin.math.roundToInt

@AndroidEntryPoint
class StatisticsFragment : Fragment() {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StatisticsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupBarChart()
    }

    private fun subscribeToObservers() {
        binding.apply {
            viewModel.totalTimeRun.observe(viewLifecycleOwner) {
                it?.let {
                    val totalTimeRun = TrackingUtility.getFormattedStopWatchTime(it)
                    tvTotalTime.text = totalTimeRun
                }
            }
            viewModel.totalAvgSpeed.observe(viewLifecycleOwner) {
                it?.let {
                    val avgSpeed = round(it * 10f) / 10f

                    val avgSpeedString = "${String.format("%.2f", avgSpeed)}km/h"
                    tvAverageSpeed.text = avgSpeedString
                }
            }
            viewModel.totalCaloriesBurned.observe(viewLifecycleOwner) {
                it?.let {
                    val totalCalories = "${it}kcal"
                    tvTotalCalories.text = totalCalories
                }
            }
            viewModel.totalDistance.observe(viewLifecycleOwner) {
                it?.let {
                    val km = it / 1000f
                    val totalDistance = round(km * 10f) / 10f
                    val totalDistanceString = "${String.format("%.2f", totalDistance)}km"
                    tvTotalDistance.text = totalDistanceString
                }
            }
            viewModel.runSortedByDate.observe(viewLifecycleOwner) {
                it?.let {
                    val allAvgSpeeds =
                        it.indices.map { i -> BarEntry(i.toFloat(), it[i].avgSpeedInKMH) }
                    val barDataset = BarDataSet(allAvgSpeeds, "AVG Speed Over Time").apply {
                        valueTextColor = Color.WHITE
                        color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
                    }
                    barChart.data = BarData(barDataset)
                    barChart.marker =
                        CustomMarkerView(it.reversed(), requireContext(), R.layout.marker_view)
                    barChart.invalidate()
                }
            }
        }
    }

    private fun setupBarChart() {
        binding.apply {
            barChart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawLabels(false)
                axisLineColor = Color.WHITE
                textColor = Color.WHITE
                setDrawGridLines(false)
            }
            barChart.axisLeft.apply {
                axisLineColor = Color.WHITE
                textColor = Color.WHITE
                setDrawGridLines(false)
            }
            barChart.axisRight.apply {
                axisLineColor = Color.WHITE
                textColor = Color.WHITE
                setDrawGridLines(false)
            }
            barChart.apply {
                description.text = "AVG Speed Over Time"
                legend.isEnabled = false
            }
        }
    }
}