package com.example.routinepro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.routinepro.data.AppDatabase
import com.example.routinepro.data.HistoryEntry
import com.example.routinepro.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
    }

    private fun setupData() {
        val context = context ?: return
        val dao = AppDatabase.getDatabase(context).routineDao()

        lifecycleScope.launch {
            try {
                // Obtener Volumen Total
                val totalVolume = dao.getTotalVolume(0) ?: 0f
                binding.tvTotalVolume.text = String.format(Locale.getDefault(), "Volumen Total: %.1f kg", totalVolume)

                // Obtener Últimas Medidas
                val measurements = dao.getAllMeasurements()
                if (measurements.isNotEmpty()) {
                    val latest = measurements[0]
                    binding.tvLatestWeight.text = "Peso: ${latest.weight} kg"
                    binding.tvLatestWaist.text = "Cintura: ${latest.waist} cm"
                }

                // Obtener Historial Completo
                val history = dao.getFullHistory()
                if (history.isNotEmpty()) {
                    binding.rvFullHistory.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvFullHistory.adapter = HistoryAdapter(history)
                    setupChart(history)
                } else {
                    binding.tvTotalVolume.text = "Registra tu primer ejercicio para ver avances"
                }
            } catch (e: Exception) {
                // Evitar crash si algo falla en la carga
                e.printStackTrace()
            }
        }

        binding.btnAddMeasurement.setOnClickListener {
            Toast.makeText(requireContext(), "Función para actualizar medidas próximamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupChart(history: List<HistoryEntry>) {
        if (history.isEmpty()) return
        
        val entries = mutableListOf<BarEntry>()
        val recentData = history.take(7).reversed()
        recentData.forEachIndexed { index, entry ->
            val volume = entry.sets * entry.reps * entry.weight
            entries.add(BarEntry(index.toFloat(), if (volume > 0) volume else 10f)) // Placeholder value if 0
        }

        val dataSet = BarDataSet(entries, "Volumen por Sesión")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val barData = BarData(dataSet)
        binding.barChart.data = barData
        binding.barChart.description.isEnabled = false
        binding.barChart.animateY(1000)
        binding.barChart.invalidate()
    }

    private class HistoryAdapter(private val items: List<HistoryEntry>) :
        RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title: TextView = view.findViewById(R.id.tvMuscleGroup)
            val subtitle: TextView = view.findViewById(R.id.tvExercises)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_routine, parent, false)
            view.findViewById<View>(R.id.btnCheckProgress).visibility = View.GONE
            view.findViewById<View>(R.id.ivExercise).visibility = View.GONE
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateStr = sdf.format(Date(item.date))

            holder.title.text = "${item.exerciseName} ($dateStr)"
            holder.subtitle.text = "${item.sets} series x ${item.reps} reps | ${item.weight} kg"
        }

        override fun getItemCount() = items.size
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
