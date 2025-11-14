package com.luisp.myapplication.ui.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.luisp.myapplication.data.Student
import com.luisp.myapplication.data.StudentDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class AnalysisState(
    val students: List<Student> = emptyList(),
    val averageScore: Double = 0.0,
    val biggestLagStudent: Student? = null,
    val top3ByGroup: Map<String, List<Student>> = emptyMap()
)

class AnalysisViewModel(database: StudentDatabase) : ViewModel() {

    private val studentDao = database.studentDao()

    private val studentsFlow = studentDao.getAllStudents()

    val uiState: StateFlow<AnalysisState> = studentsFlow
        .combine(studentsFlow) { students, _ ->
            if (students.isEmpty()) {
                AnalysisState()
            } else {
                AnalysisState(
                    students = students,
                    averageScore = calculateAverage(students),
                    biggestLagStudent = findBiggestLag(students),
                    top3ByGroup = findTop3ByGroup(students)
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AnalysisState()
        )

    private fun calculateAverage(students: List<Student>): Double {
        if (students.isEmpty()) return 0.0
        return students.map { it.score }.average()
    }

    private fun findBiggestLag(students: List<Student>): Student? {
        return students.minByOrNull { it.score }
    }

    private fun findTop3ByGroup(students: List<Student>): Map<String, List<Student>> {
        return students
            .groupBy { it.group }
            .mapValues { (_, studentsInGroup) ->
                studentsInGroup.sortedByDescending { it.score }
                    .take(3)
            }
    }

    companion object {
        fun Factory(database: StudentDatabase): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(AnalysisViewModel::class.java)) {
                        return AnalysisViewModel(database) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}