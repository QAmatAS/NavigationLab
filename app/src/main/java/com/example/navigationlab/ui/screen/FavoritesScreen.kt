package com.example.navigationlab.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.navigationlab.data.local.FavoriteStudentEntity

@Composable
fun FavoritesScreen(
    favorites: List<FavoriteStudentEntity>,
    onStudentClick: (Int) -> Unit = {},
    onBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Daftar Favorit", style = MaterialTheme.typography.headlineMedium)

        if (favorites.isEmpty()) {
            Text(
                text = "Belum ada mahasiswa favorit.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(favorites, key = { it.id }) { student ->
                    val fullName = if (student.lastName.isNotBlank()) {
                        "${student.firstName} ${student.lastName}"
                    } else {
                        student.firstName
                    }
                    Card(
                        onClick = { onStudentClick(student.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = fullName,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = student.email,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        Button(onClick = onBack) {
            Text("Kembali")
        }
    }
}
