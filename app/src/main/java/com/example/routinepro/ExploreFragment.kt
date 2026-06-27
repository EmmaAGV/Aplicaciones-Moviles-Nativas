package com.example.routinepro

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.routinepro.databinding.FragmentExploreBinding
import com.google.android.material.chip.Chip

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    
    private val fullList = listOf(
        ExerciseItem("Sentadillas", "Fuerza", R.drawable.sentadilla),
        ExerciseItem("Flexiones", "Fuerza", R.drawable.flexiones),
        ExerciseItem("Burpees", "Cardio", R.drawable.burpees),
        ExerciseItem("Proteínas", "Nutrición", R.drawable.images_de_proteina),
        ExerciseItem("Carbohidratos", "Nutrición", R.drawable.carbohidratos),
        ExerciseItem("Vegetales", "Nutrición", R.drawable.imagen_de_verduras),
        ExerciseItem("Grasas Saludables", "Nutrición", R.drawable.grasas_saludables),
        ExerciseItem("Plancha", "Cardio", R.drawable.plancha),
        ExerciseItem("Zancadas", "Fuerza", R.drawable.sentadilla), // Reuse or change if you have lunge image
        ExerciseItem("Caminata", "Cardio", R.drawable.cardio),
        ExerciseItem("Menú Saludable", "Nutrición", R.drawable.images_del_menu)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView(fullList)
        setupFilters()
    }

    private fun setupRecyclerView(list: List<ExerciseItem>) {
        binding.rvExercises.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvExercises.adapter = ExploreAdapter(list) { item ->
            if (item.category == "Nutrición") {
                val bundle = bundleOf(
                    "TITLE" to item.name,
                    "CONTENT" to getStaticNutritionContent(item.name),
                    "IMAGE_RES" to item.imageRes
                )
                findNavController().navigate(R.id.action_exploreFragment_to_nutritionDetailFragment, bundle)
            } else {
                findNavController().navigate(R.id.action_exploreFragment_to_workoutPlayerFragment)
            }
        }
    }

    private fun getStaticNutritionContent(title: String): String {
        return when (title) {
            "Proteínas" -> "Guía de Proteínas: Reparación y Estructura. Consume 1.8g - 2.2g por kg."
            "Carbohidratos" -> "Guía de Carbohidratos: Tu combustible. Úsalos según la intensidad."
            "Vegetales" -> "Guía de Vegetales: Salud general y recuperación micro-celular."
            "Grasas Saludables" -> "Grasas Saludables: Vitales para hormonas y energía."
            else -> "Información nutricional personalizada."
        }
    }

    private fun setupFilters() {
        binding.chipGroupCategories.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val selectedChip = group.findViewById<Chip>(checkedIds.first())
                val category = selectedChip.text.toString()
                filterList(category, binding.etSearch.text.toString())
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val selectedId = binding.chipGroupCategories.checkedChipId
                val chip = binding.chipGroupCategories.findViewById<Chip>(selectedId)
                val category = chip?.text?.toString() ?: "Todo"
                filterList(category, s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterList(category: String, query: String) {
        val filtered = fullList.filter { item ->
            (category == "Todo" || item.category == category) &&
            (item.name.contains(query, ignoreCase = true))
        }
        setupRecyclerView(filtered)
    }

    data class ExerciseItem(val name: String, val category: String, val imageRes: Int)

    private class ExploreAdapter(
        private val items: List<ExerciseItem>,
        private val onItemClick: (ExerciseItem) -> Unit
    ) : RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title: TextView = view.findViewById(R.id.tvMuscleGroup)
            val subtitle: TextView = view.findViewById(R.id.tvExercises)
            val image: android.widget.ImageView = view.findViewById(R.id.ivExercise)
            val btn: android.widget.Button = view.findViewById(R.id.btnCheckProgress)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_routine, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.title.text = item.name
            holder.subtitle.text = item.category
            holder.image.visibility = View.VISIBLE
            holder.image.setImageResource(item.imageRes)
            holder.btn.text = "Ver Guía"
            holder.btn.setOnClickListener { onItemClick(item) }
            holder.itemView.setOnClickListener { onItemClick(item) }
        }

        override fun getItemCount() = items.size
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
