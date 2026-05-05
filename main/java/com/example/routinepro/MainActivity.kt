package com.example.routinepro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.routinepro.databinding.ActivityMainBinding

/**
 * Actividad Principal usando ViewBinding.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navegación a RutinasActivity
        binding.btnViewRoutines.setOnClickListener {
            val intent = Intent(this, RutinasActivity::class.java)
            startActivity(intent)
        }
    }
}
