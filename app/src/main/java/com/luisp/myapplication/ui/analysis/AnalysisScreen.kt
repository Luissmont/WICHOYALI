package com.luisp.myapplication.ui.analysis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.luisp.myapplication.data.Student
import java.text.DecimalFormat

import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(viewModel: AnalysisViewModel) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Análisis de Estudiantes") }) }) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {
            item {
                SummaryCard(
                    average = state.averageScore,
                    lagStudent = state.biggestLagStudent
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Top 3 Estudiantes por Grupo",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Divider(Modifier.padding(vertical = 8.dp))
            }

            state.top3ByGroup.forEach { (group, topStudents) ->
                item(key = "group-$group") {
                    Text(
                        "GRUPO: $group",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }
                items(topStudents, key = { it.id }) { student ->
                    TopStudentItem(student = student)
                    if (topStudents.last() != student) {
                        Divider(Modifier.padding(start = 16.dp))
                    }
                }
                item { Spacer(modifier = Modifier.height(12.dp)) }
            }
        }
    }
}

@Composable
fun SummaryCard(average: Double, lagStudent: Student?) {
    val df = DecimalFormat("#.##")
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.padding(16.dp)) {
            // 1. Cálculo de promedios
            Text("Promedio General de Puntaje:", style = MaterialTheme.typography.titleMedium)
            Text(
                df.format(average),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text("Estudiante con Mayor Rezago:", style = MaterialTheme.typography.titleMedium)
            if (lagStudent != null) {
                Text(
                    text = "${lagStudent.name} ${lagStudent.lastName}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text("Puntaje más bajo: ${lagStudent.score}", style = MaterialTheme.typography.bodyMedium)
            } else {
                Text("No hay suficientes datos para el análisis.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun TopStudentItem(student: Student) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(
            text = "${student.name} ${student.lastName}",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Puntaje: ${student.score}",
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}