package com.example.navigationlab.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.navigationlab.ui.home.HomeUiState
import com.example.navigationlab.ui.home.HomeViewModel

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    searchQuery: String = uiState.searchQuery,
    onSearchQueryChange: (String) -> Unit = {},
    onStudentClick: (Int) -> Unit = {},
    onRetry: () -> Unit = {},
    viewModel: HomeViewModel? = null,
    onRefresh: () -> Unit = { viewModel?.refresh() },
    onOpenDetail: (Int) -> Unit = onStudentClick,
    onOpenProfile: () -> Unit = {},
    onOpenAbout: () -> Unit = {},
    onSearch: () -> Unit = { viewModel?.searchStudents() }
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Home", style = MaterialTheme.typography.headlineMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onOpenProfile) {
                Text("Profile")
            }
            Button(onClick = onOpenAbout) {
                Text("About")
            }
            OutlinedButton(onClick = onRefresh) {
                Text("Refresh")
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("Cari profil") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Penanganan tampilan berdasarkan UI State
        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.errorMessage != null -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(uiState.errorMessage)
                    Button(onClick = onRetry) { Text("Coba Lagi") }
                }
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(uiState.students) { student ->
                        Card(
                            onClick = { onOpenDetail(student.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = student.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "${student.email}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "${student.university}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}