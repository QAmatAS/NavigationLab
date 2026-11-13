package com.example.navigationlab.ui.home

import com.example.navigationlab.data.model.Student
data class HomeUiState(
    val searchQuery: String = "",
    val students: List<Student> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)