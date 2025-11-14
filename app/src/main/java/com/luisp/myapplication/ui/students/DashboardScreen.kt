package com.luisp.myapplication.ui.students

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.luisp.myapplication.data.Student

object StudentRoutes {
    const val DASHBOARD = "dashboard"
    const val ADD_EDIT = "add_edit/{studentId}"
    fun createAddEditRoute(id: Int = 0) = "add_edit/$id"
}

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: StudentsViewModel = viewModel() // ViewModel inyectado
) {
    val students by viewModel.students.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(StudentRoutes.createAddEditRoute()) }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        if (students.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay estudiantes registrados.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 8.dp),
                contentPadding = PaddingValues(top = 8.dp)
            ) {
                items(students, key = { it.id }) { student ->
                    StudentItem(
                        student = student,
                        onEdit = { navController.navigate(StudentRoutes.createAddEditRoute(it.id)) },
                        onDelete = { viewModel.deleteStudent(it) }
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
fun StudentItem(
    student: Student,
    onEdit: (Student) -> Unit,
    onDelete: (Student) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit(student) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${student.name} ${student.lastName}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text("Grado: ${student.grade} | Grupo: ${student.group}", style = MaterialTheme.typography.bodySmall)
        }
        Text(
            text = "Puntaje: ${student.score}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        IconButton(onClick = { onDelete(student) }) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
        }
    }
}