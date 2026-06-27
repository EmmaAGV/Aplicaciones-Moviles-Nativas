package com.example.routinepro

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.routinepro.data.AppDatabase
import com.example.routinepro.data.HistoryEntry
import com.example.routinepro.data.WaterLog
import com.example.routinepro.databinding.FragmentDashboardBinding
import com.example.routinepro.model.UserProfile
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch
import java.util.Locale

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    
    private var waterCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("DEPRECATION")
        val profile = (arguments?.getSerializable("USER_PROFILE") as? UserProfile) ?: loadProfileFromPrefs()
        val aiRecommendation = arguments?.getString("AI_RECOMMENDATION") ?: "Sigue tu plan diario para mejores resultados."

        if (profile != null) {
            val greeting = if (profile.gender == "Hombre") "¡Hola, Guerrero!" else "¡Hola, Guerrera!"
            binding.tvGreeting.text = greeting
            binding.tvRoutineSummary.text = "Plan para ${profile.age} años | Meta: ${profile.mainGoal}"
            
            try {
                setupProjectionChart(profile.weight)
            } catch (e: Exception) { e.printStackTrace() }
            
            setupFullCustomPlan(profile, aiRecommendation)
            setupWaterTracker()
        } else {
            binding.tvGreeting.text = "¡Hola!"
            binding.tvRoutineSummary.text = "Completa tu encuesta para ver tu plan."
            // Mostrar plan por defecto para que no se vea vacío
            setupFullCustomPlan(null, null)
            setupWaterTracker()
        }
    }

    private fun loadProfileFromPrefs(): UserProfile? {
        val context = context ?: return null
        val prefs = context.getSharedPreferences("RoutineProPrefs", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("user_registered", false)) return null
        
        return UserProfile(
            age = prefs.getInt("user_age", 25),
            weight = prefs.getFloat("user_weight", 70f),
            height = prefs.getInt("user_height", 170),
            gender = prefs.getString("user_gender", "Mujer") ?: "Mujer",
            size = prefs.getString("user_size", "Mediana") ?: "Mediana",
            bodyGoal = prefs.getString("user_body_goal", "Tonificada") ?: "Tonificada",
            mainGoal = prefs.getString("user_goal", "Saludable") ?: "Saludable",
            zones = "General",
            history = "Ninguno",
            activityLevel = "Medio",
            habits = "Ninguno"
        )
    }

    private fun setupWaterTracker() {
        binding.tvWaterCount.text = "$waterCount / 8 vasos"
        binding.btnAddWater.setOnClickListener {
            if (waterCount < 8) {
                waterCount++
                binding.tvWaterCount.text = "$waterCount / 8 vasos"
                saveWaterLog()
                if (waterCount == 8) {
                    Toast.makeText(requireContext(), "¡Meta de hidratación cumplida! 💧🏆", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveWaterLog() {
        lifecycleScope.launch {
            try {
                AppDatabase.getDatabase(requireContext()).routineDao().insertWaterLog(WaterLog(glasses = 1))
            } catch (e: Exception) {}
        }
    }

    private fun setupProjectionChart(currentWeight: Float) {
        val entries = mutableListOf<Entry>()
        entries.add(Entry(0f, currentWeight))
        entries.add(Entry(1f, currentWeight - 0.5f))
        entries.add(Entry(4f, currentWeight - 2f))
        entries.add(Entry(12f, currentWeight - 6f))

        val dataSet = LineDataSet(entries, "Meta de Peso")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.primary)
        dataSet.setCircleColor(ContextCompat.getColor(requireContext(), R.color.primary))
        dataSet.lineWidth = 3f
        dataSet.setDrawFilled(true)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        val lineData = LineData(dataSet)
        binding.projectionChart.data = lineData
        binding.projectionChart.description.isEnabled = false
        binding.projectionChart.xAxis.isEnabled = false
        binding.projectionChart.axisRight.isEnabled = false
        binding.projectionChart.animateX(1000)
        binding.projectionChart.invalidate()
    }

    private fun setupFullCustomPlan(profile: UserProfile?, aiRecommendation: String?) {
        val items = mutableListOf<PlanItem>()

        if (!aiRecommendation.isNullOrBlank()) {
            items.add(PlanItem("🤖 IA: Tu Plan", aiRecommendation, false, R.drawable.de_agente_de_ia))
        }

        items.add(PlanItem("🍳 Proteínas", "Sugerencia: Huevos, Pollo, Carne magra.", false, R.drawable.images_de_proteina))
        items.add(PlanItem("🍚 Carbohidratos", "Sugerencia: Arroz, Avena, Papa.", false, R.drawable.carbohidratos))
        items.add(PlanItem("🥗 Vegetales", "Sugerencia: Ensaladas verdes y brócoli.", false, R.drawable.imagen_de_verduras))
        items.add(PlanItem("🥑 Grasas Saludables", "Aguacate, Frutos secos, Aceite.", false, R.drawable.grasas_saludables))
        items.add(PlanItem("🍴 Menú del Día", "Ejemplo de comidas equilibradas.", false, R.drawable.images_del_menu))

        if (profile?.mainGoal?.contains("adelgazar", true) == true) {
            items.add(PlanItem("🏃 Cardio Intenso", "30 min caminata rápida.", true, R.drawable.cardio))
        } else {
            items.add(PlanItem("💪 Fuerza", "Sentadillas y Planchas.", true, R.drawable.sentadilla))
        }

        binding.rvRoutines.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRoutines.adapter = RoutineAdapter(items) { item ->
            if (item.isExercise) {
                findNavController().navigate(R.id.action_dashboardFragment_to_workoutPlayerFragment)
            } else {
                val weight = profile?.weight ?: 70f
                val detailContent = when (item.title) {
                    "🍳 Proteínas" -> """
                        1. La Proteína: Reparación y Estructura
                        Cálculo Personalizado ($weight kg):
                        - General: ${String.format(Locale.getDefault(), "%.1f", weight * 1.4)}g - ${String.format(Locale.getDefault(), "%.1f", weight * 1.8)}g al día.
                        - Ganancia Muscular: ${String.format(Locale.getDefault(), "%.1f", weight * 1.8)}g - ${String.format(Locale.getDefault(), "%.1f", weight * 2.2)}g al día.
                        Después de entrenar: Consume 20-30g (Pollo, pescado, huevos).
                    """.trimIndent()
                    "🍚 Carbohidratos" -> """
                        Guía de Carbohidratos (Energía)
                        Cálculo para tus $weight kg:
                        - Ligero: ${String.format(Locale.getDefault(), "%.0f", weight * 3)}g - ${String.format(Locale.getDefault(), "%.0f", weight * 5)}g al día.
                        - Intenso: ${String.format(Locale.getDefault(), "%.0f", weight * 6)}g - ${String.format(Locale.getDefault(), "%.0f", weight * 8)}g al día.
                    """.trimIndent()
                    "🥗 Vegetales" -> """
                        2. Las Verduras: El Soporte Invisible
                        Salud general y recuperación micro-celular.
                        Regla de oro: 400g al día (5 porciones).
                    """.trimIndent()
                    "🥑 Grasas Saludables" -> """
                        Grasas Saludables: Hormonas y Energía
                        Vitales para el funcionamiento hormonal. Incluye aguacate, nueces y aceite de oliva.
                    """.trimIndent()
                    "🍴 Menú del Día" -> """
                        Ejemplo de Menú
                        🍳 Desayuno: Avena y yogur.
                        🍱 Almuerzo: Pollo y arroz integral.
                        🍽️ Cena: Revuelto de huevos con camote.
                    """.trimIndent()
                    else -> item.content
                }

                val bundle = Bundle().apply {
                    putString("TITLE", item.title)
                    putString("CONTENT", detailContent)
                    putInt("IMAGE_RES", item.imageRes)
                }
                findNavController().navigate(R.id.action_dashboardFragment_to_nutritionDetailFragment, bundle)
            }
        }
    }

    private fun saveToHistory(item: PlanItem) {
        val entry = HistoryEntry(
            exerciseName = item.title,
            muscleGroup = "Personalizado",
            sets = 1,
            reps = 1,
            weight = 0f,
            date = System.currentTimeMillis()
        )
        lifecycleScope.launch {
            try {
                AppDatabase.getDatabase(requireContext()).routineDao().insertHistory(entry)
            } catch (e: Exception) {}
        }
    }

    data class PlanItem(val title: String, val content: String, val isExercise: Boolean, val imageRes: Int)

    private class RoutineAdapter(
        private val items: List<PlanItem>,
        private val onCheckClick: (PlanItem) -> Unit
    ) : androidx.recyclerview.widget.RecyclerView.Adapter<RoutineAdapter.ViewHolder>() {

        class ViewHolder(view: android.view.View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
            val title: android.widget.TextView = view.findViewById(R.id.tvMuscleGroup)
            val subtitle: android.widget.TextView = view.findViewById(R.id.tvExercises)
            val btnCheck: android.widget.Button = view.findViewById(R.id.btnCheckProgress)
            val image: android.widget.ImageView = view.findViewById(R.id.ivExercise)
        }

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(android.view.LayoutInflater.from(parent.context).inflate(R.layout.item_routine, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.title.text = item.title
            holder.subtitle.text = item.content
            holder.image.visibility = View.VISIBLE
            holder.image.setImageResource(item.imageRes)
            holder.btnCheck.text = if (item.isExercise) "Ver Guía" else "Hecho"
            holder.btnCheck.setOnClickListener { onCheckClick(item) }
        }

        override fun getItemCount() = items.size
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
