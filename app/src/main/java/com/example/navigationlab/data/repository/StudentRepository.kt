package com.example.navigationlab.data.repository

import com.example.navigationlab.data.model.Student
import com.example.navigationlab.data.source.StudentLocalDataSource
interface StudentRepository {
    fun getStudentById(id: Int): Student?
    fun getStudents(): List<Student>
}

class DefaultStudentRepository(
    private val dataSource: StudentLocalDataSource
) : StudentRepository {
    override fun getStudentById(id: Int): Student? =
        dataSource.getStudentById(id)

    override fun getStudents(): List<Student> =
        dataSource.getStudent()
}