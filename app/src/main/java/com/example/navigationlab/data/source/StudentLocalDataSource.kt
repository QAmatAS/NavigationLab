package com.example.navigationlab.data.source

import com.example.navigationlab.data.model.Student
class StudentLocalDataSource {
    private val students = listOf(
        Student(2026, "Alya Putri", "Teknik Informatika", 2026),
        Student(2027, "Bima Pratama", "Teknik Informatika", 2026),
        Student(2028, "Citra Lestari", "Teknik Informatika", 2026),
        Student(2029, "Kim Ji Won", "Teknik Informatika", 2026),
        Student(2030, "Jang Wonyoung", "Teknik Informatika", 2026)
    )
    fun getStudentById(id: Int): Student? =
        students.find { it.id == id }

    fun getStudent(): List<Student> = students
}