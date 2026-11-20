package com.example.navigationlab.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.navigationlab.data.repository.FavoriteRepository
import com.example.navigationlab.data.repository.StudentRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: StudentRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    var uiState by mutableStateOf(DetailUiState())
        private set

    private var favoriteJob: Job? = null

    fun loadStudent(id: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val student = repository.getStudent(id)
                uiState = DetailUiState(student = student)
                observeFavorite(id)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                uiState = DetailUiState(
                    errorMessage = e.message ?: "Data tidak tersedia"
                )
            }
        }
    }

    private fun observeFavorite(id: Int) {
        favoriteJob?.cancel()
        favoriteJob = viewModelScope.launch {
            favoriteRepository.observeIsFavorite(id).collect { value ->
                uiState = uiState.copy(isFavorite = value)
            }
        }
    }

    fun toggleFavorite() {
        val student = uiState.student ?: return
        viewModelScope.launch {
            if (uiState.isFavorite) {
                favoriteRepository.remove(student.id)
            } else {
                favoriteRepository.add(student)
            }
        }
    }

    companion object {
        fun factory(
            repository: StudentRepository,
            favoriteRepository: FavoriteRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DetailViewModel(repository, favoriteRepository) as T
                }
            }
    }
}