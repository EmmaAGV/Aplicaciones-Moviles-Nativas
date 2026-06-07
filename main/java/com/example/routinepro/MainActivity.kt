package com.example.routinepro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * Actividad Principal que actúa como host de navegación para toda la app.
 * Se cumple con el requisito de usar NavController y navegación basada en rutas.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Uso de Material Design 3 (Requisito)
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Definición de rutas para la navegación (Requisito)
    NavHost(
        navController = navController, 
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(onLoginSuccess = {
                // Navegación a la pantalla principal
                navController.navigate("main") {
                    // Limpia el historial para que no se pueda volver al login con el botón 'atrás'
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("main") {
            MainScreen(onViewRoutines = {
                navController.navigate("routines")
            })
        }
        composable("routines") {
            RutinasScreen()
        }
    }
}
