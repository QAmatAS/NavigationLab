package com.example.navigationlab.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.navigationlab.data.repository.StudentRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: StudentRepository
) : ViewModel() {
    var uiState by mutableStateOf(DetailUiState())
        private set

    fun loadStudent(id: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val student = repository.getStudent(id)
                uiState = DetailUiState(student = student)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                uiState = DetailUiState(
                    errorMessage = e.message ?: "Data tidak tersedia"
                )
            }
        }
    }
    companion object {
        fun factory(repository: StudentRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DetailViewModel(repository) as T
                }
            }
    }
}