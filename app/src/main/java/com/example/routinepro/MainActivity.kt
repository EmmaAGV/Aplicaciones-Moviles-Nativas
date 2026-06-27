package com.example.routinepro

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.routinepro.databinding.ActivityMainHostBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Lógica de destino inicial dinámico
        val prefs = getSharedPreferences("RoutineProPrefs", MODE_PRIVATE)
        val isRegistered = prefs.getBoolean("user_registered", false)

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        if (isRegistered) {
            navGraph.setStartDestination(R.id.dashboardFragment)
        } else {
            navGraph.setStartDestination(R.id.mainFragment)
        }
        navController.graph = navGraph

        // Vincular UI con NavController
        binding.bottomNavigation.setupWithNavController(navController)
        binding.toolbar.setupWithNavController(navController)

        // Controlar visibilidad de navegación: Ocultar en la encuesta, mostrar en el resto
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.mainFragment) {
                binding.bottomNavigation.visibility = View.GONE
                binding.toolbar.visibility = View.GONE
            } else {
                binding.bottomNavigation.visibility = View.VISIBLE
                binding.toolbar.visibility = View.VISIBLE
            }
        }
    }
}
