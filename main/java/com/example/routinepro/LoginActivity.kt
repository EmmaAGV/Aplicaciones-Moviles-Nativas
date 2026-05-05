package com.example.routinepro

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.routinepro.databinding.ActivityLoginBinding

/**
 * Pantalla de inicio de sesión usando ViewBinding.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicialización de ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración del click para el botón de login
        binding.btnLogin.setOnClickListener {
            val user = binding.etUser.text.toString()
            val pass = binding.etPassword.text.toString()

            // Validación simple: no permitir campos vacíos
            if (user.isNotEmpty() && pass.isNotEmpty()) {
                // Navegar a MainActivity tras el "login"
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Cerramos LoginActivity para que no regrese al presionar atrás
            } else {
                // Mostrar mensaje de error usando string resource
                Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
