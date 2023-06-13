package com.example.ssuapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ssuapp.databinding.FragmentDashboardBinding
import com.example.ssuapp.ui.dashboard.DashboardViewModel
import com.example.ssuapp.ui.wallets.WalletsViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.loadBalances()
        viewModel.cryptoBalances.observe(viewLifecycleOwner) { cryptoBalanceList ->
            val entries = cryptoBalanceList.map { PieEntry(it.percentage.toFloat(), it.name) }

            val dataSet = PieDataSet(entries, "Cryptocurrencies")
            dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

            val data = PieData(dataSet)

            val pieChart: PieChart = binding.pieChart
            pieChart.data = data
            pieChart.invalidate()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}