package com.example.navigationlab.data.repository

import com.example.navigationlab.data.model.Student
import com.example.navigationlab.data.remote.StudentRemoteDataSource
import com.example.navigationlab.data.remote.dto.toStudent
import com.example.navigationlab.data.source.StudentLocalDataSource
interface StudentRepository {
    suspend fun getStudents(): List<Student>
    suspend fun getStudent(id: Int): Student
    suspend fun searchStudents(query: String): List<Student>
}
