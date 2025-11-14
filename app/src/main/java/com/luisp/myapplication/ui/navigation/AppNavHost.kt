package com.luisp.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.luisp.myapplication.data.StudentDatabase
import com.luisp.myapplication.ui.analysis.AnalysisScreen
import com.luisp.myapplication.ui.analysis.AnalysisViewModel
import com.luisp.myapplication.ui.students.AddEditScreen
import com.luisp.myapplication.ui.students.DashboardScreen
import com.luisp.myapplication.ui.students.StudentRoutes
import com.luisp.myapplication.ui.students.StudentsViewModel
import androidx.navigation.compose.rememberNavController

enum class TabRoute(val route: String) {
    STUDENTS("students_tab"),
    ANALYSIS("analysis_tab")
}

@Composable
fun AppNavHost(navController: NavHostController) {
    val context = LocalContext.current
    val database = StudentDatabase.getDatabase(context)

    val studentsViewModel: StudentsViewModel = viewModel(
        factory = StudentsViewModel.Factory(database)
    )
    val analysisViewModel: AnalysisViewModel = viewModel(
        factory = AnalysisViewModel.Factory(database)
    )

    NavHost(
        navController = navController,
        startDestination = TabRoute.STUDENTS.route
    ) {
        composable(TabRoute.STUDENTS.route) {
            val tabNavController = rememberNavController()
            NavHost(
                navController = tabNavController,
                startDestination = StudentRoutes.DASHBOARD
            ) {
                composable(StudentRoutes.DASHBOARD) {
                    DashboardScreen(tabNavController, studentsViewModel)
                }
                composable(
                    route = StudentRoutes.ADD_EDIT,
                    arguments = listOf(navArgument("studentId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0
                    AddEditScreen(tabNavController, studentId, studentsViewModel)
                }
            }
        }

        composable(TabRoute.ANALYSIS.route) {
            AnalysisScreen(analysisViewModel)
        }
    }
}