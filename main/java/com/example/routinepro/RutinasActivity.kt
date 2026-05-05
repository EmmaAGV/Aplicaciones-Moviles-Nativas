package com.example.routinepro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.routinepro.databinding.ActivityRutinasBinding

/**
 * Pantalla que muestra la lista de rutinas de ejercicio.
 */
class RutinasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRutinasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRutinasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Aquí se podría inicializar un RecyclerView en el futuro.
        // Por ahora, el diseño XML estático muestra las rutinas de ejemplo.
    }
}
