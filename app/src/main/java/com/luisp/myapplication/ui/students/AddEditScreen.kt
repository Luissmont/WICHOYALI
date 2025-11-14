package com.luisp.myapplication.ui.students

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.luisp.myapplication.data.Student
import kotlinx.coroutines.launch
import androidx.compose.material3.ExperimentalMaterial3Api
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    navController: NavController,
    studentId: Int,
    viewModel: StudentsViewModel = viewModel()
) {
    var student by remember { mutableStateOf(Student()) }
    var scoreText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(studentId) {
        if (studentId != 0) {
            val existingStudent = viewModel.getStudentById(studentId)
            existingStudent?.let {
                student = it
                scoreText = it.score.toString()
            }
        }
    }

    val isFormValid = student.name.isNotBlank() && student.lastName.isNotBlank() &&
            student.grade.isNotBlank() && student.group.isNotBlank() && scoreText.toIntOrNull() != null

    val title = if (studentId == 0) "Agregar Estudiante" else "Editar Estudiante"

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Campos de texto simplificados
            InputField(label = "Nombre", value = student.name, onValueChange = { student = student.copy(name = it) })
            InputField(label = "Apellidos", value = student.lastName, onValueChange = { student = student.copy(lastName = it) })
            InputField(label = "Grado", value = student.grade, onValueChange = { student = student.copy(grade = it) })
            InputField(label = "Grupo", value = student.group, onValueChange = { student = student.copy(group = it) })

            OutlinedTextField(
                value = scoreText,
                onValueChange = {
                    scoreText = it.filter { char -> char.isDigit() }
                },
                label = { Text("Puntaje") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                isError = scoreText.toIntOrNull() == null
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    scope.launch {
                        if (isFormValid) {
                            val score = scoreText.toIntOrNull() ?: 0
                            val studentToSave = student.copy(score = score)
                            viewModel.saveStudent(studentToSave)
                            navController.popBackStack()
                        } else {
                            snackbarHostState.showSnackbar("Error: Complete todos los campos.")
                        }
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(if (studentId == 0) "Guardar" else "Actualizar")
            }
        }
    }
}

@Composable
private fun InputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        isError = value.isBlank()
    )
}