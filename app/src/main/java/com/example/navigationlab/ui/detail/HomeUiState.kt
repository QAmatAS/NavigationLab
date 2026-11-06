package com.example.navigationlab.ui.detail

import com.example.navigationlab.data.model.Student

data class HomeUiState(
    val isLoading: Boolean = false,
    val students: List<Student> = emptyList(),
    val errorMessage: String? = null
)