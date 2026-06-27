package com.example.routinepro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinepro.model.UserProfile
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    sealed class UiState {
        object Loading : UiState()
        data class Success(val profile: UserProfile, val aiRecommendation: String? = null) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "TU_API_KEY_AQUI"
    )

    fun processRecommendation(profile: UserProfile) {
        _uiState.value = UiState.Loading
        
        viewModelScope.launch {
            try {
                // Simulación de recomendación personalizada basada en la encuesta
                val aiRecommendation = "Basado en tu perfil de ${profile.gender} de ${profile.age} años con objetivo '${profile.mainGoal}', te recomendamos priorizar el entrenamiento en '${profile.zones}'."
                
                // Nota: Para usar IA real, descomenta lo siguiente y usa tu API KEY
                // val response = generativeModel.generateContent("Genera un plan para: ${profile.mainGoal}")
                // _uiState.value = UiState.Success(profile, response.text)
                
                _uiState.value = UiState.Success(profile, aiRecommendation)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al procesar el plan: ${e.message}")
            }
        }
    }
}
