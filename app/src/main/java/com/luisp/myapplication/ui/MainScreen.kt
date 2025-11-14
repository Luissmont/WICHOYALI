package com.luisp.myapplication.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.luisp.myapplication.ui.navigation.AppNavHost
import com.luisp.myapplication.ui.navigation.TabRoute

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

private val items = listOf(
    BottomNavItem(TabRoute.STUDENTS.route, Icons.Default.Group, "Estudiantes"),
    BottomNavItem(TabRoute.ANALYSIS.route, Icons.Default.Analytics, "Análisis")
)

/**
 * Contenedor principal que maneja la barra de pestañas (Tab Bar).
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up para la pantalla de inicio y evitar duplicados
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // AppNavHost maneja la navegación del contenido de las pestañas
        AppNavHost(navController = navController)
    }
}