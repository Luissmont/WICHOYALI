package com.luisp.myapplication.ui.students

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.luisp.myapplication.data.Student
import com.luisp.myapplication.data.StudentDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class StudentsViewModel(
    private val database: StudentDatabase
) : ViewModel() {

    private val studentDao = database.studentDao()

    val students: StateFlow<List<Student>> = studentDao.getAllStudents()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )


    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            studentDao.deleteStudent(student)
        }
    }


    suspend fun saveStudent(student: Student) {
        studentDao.insertStudent(student)
    }


    suspend fun getStudentById(id: Int): Student? {
        return studentDao.getStudentById(id)
    }

    companion object {
        fun Factory(database: StudentDatabase): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(StudentsViewModel::class.java)) {
                        return StudentsViewModel(database) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}