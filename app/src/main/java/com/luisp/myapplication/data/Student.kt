package com.luisp.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val lastName: String = "",
    val grade: String = "",
    val group: String = "",
    val score: Int = 0
)