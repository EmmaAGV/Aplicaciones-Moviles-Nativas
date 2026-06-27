package com.example.routinepro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.routinepro.databinding.FragmentMainBinding
import com.example.routinepro.model.UserProfile
import com.example.routinepro.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        binding.btnFinish.setOnClickListener {
            if (validateInputs()) {
                val profile = createUserProfile()
                viewModel.processRecommendation(profile)
            }
        }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MainViewModel.UiState.Loading -> {
                    // Mostrar progreso
                }
                is MainViewModel.UiState.Success -> {
                    navigateToDashboard(state.profile, state.aiRecommendation)
                }
                is MainViewModel.UiState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        if (binding.etAge.text.isNullOrBlank() || 
            binding.etWeight.text.isNullOrBlank() || 
            binding.etHeight.text.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun createUserProfile(): UserProfile {
        val age = binding.etAge.text.toString().toIntOrNull() ?: 25
        val weight = binding.etWeight.text.toString().toFloatOrNull() ?: 70f
        val height = binding.etHeight.text.toString().toIntOrNull() ?: 170
        val gender = if (binding.rbMale.isChecked) "Hombre" else "Mujer"

        val size = getRadioText(binding.rgSize.checkedRadioButtonId) ?: "Mediana"
        val bodyGoal = getRadioText(binding.rgBodyGoal.checkedRadioButtonId) ?: "Tonificada"
        val mainGoal = getRadioText(binding.rgGoal.checkedRadioButtonId) ?: "Saludable"
        val activityLevel = getRadioText(binding.rgActivityLevel.checkedRadioButtonId) ?: "Medio"

        val zones = mutableListOf<String>().apply {
            if (binding.cbLegs.isChecked) add("Piernas")
            if (binding.cbAbs.isChecked) add("Abdomen")
            if (binding.cbArms.isChecked) add("Brazos")
            if (binding.cbBack.isChecked) add("Espalda")
        }
        val zonesStr = zones.ifEmpty { listOf("General") }.joinToString(", ")

        val habits = mutableListOf<String>().apply {
            if (binding.cbDog.isChecked) add("Pasear perro")
            if (binding.cbKids.isChecked) add("Jugar con hijos")
            if (binding.cbStairs.isChecked) add("Subir escaleras")
            if (binding.cbHousework.isChecked) add("Tareas domésticas")
        }
        val habitsStr = habits.ifEmpty { listOf("Ninguno") }.joinToString(", ")

        val lastExercise = binding.spinnerLastExercise.selectedItem?.toString() ?: "Nunca"

        return UserProfile(
            age, weight, height, gender, size, bodyGoal, 
            mainGoal, zonesStr, lastExercise, activityLevel, habitsStr
        )
    }

    private fun getRadioText(checkedId: Int): String? {
        if (checkedId == -1) return null
        return view?.findViewById<RadioButton>(checkedId)?.text?.toString()
    }

    private fun navigateToDashboard(profile: UserProfile, aiRecommendation: String?) {
        // Guardar información extendida para persistencia (Perfil y Dashboard)
        val prefs = requireContext().getSharedPreferences("RoutineProPrefs", android.content.Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean("user_registered", true)
            .putInt("user_age", profile.age)
            .putFloat("user_weight", profile.weight)
            .putInt("user_height", profile.height)
            .putString("user_gender", profile.gender)
            .putString("user_size", profile.size)
            .putString("user_body_goal", profile.bodyGoal)
            .putString("user_goal", profile.mainGoal)
            .apply()

        val bundle = Bundle().apply {
            putSerializable("USER_PROFILE", profile)
            putString("AI_RECOMMENDATION", aiRecommendation)
        }
        findNavController().navigate(R.id.action_mainFragment_to_dashboardFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
