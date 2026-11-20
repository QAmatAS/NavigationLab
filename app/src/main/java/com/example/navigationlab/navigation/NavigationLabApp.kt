package com.example.navigationlab.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.navigationlab.Routes
import com.example.navigationlab.data.local.DatabaseProvider
import com.example.navigationlab.data.preferences.UserPreferencesRepository
import com.example.navigationlab.data.remote.RetrofitClient
import com.example.navigationlab.data.remote.StudentRemoteDataSource
import com.example.navigationlab.data.repository.DefaultStudentRepository
import com.example.navigationlab.data.repository.FavoriteRepository
import com.example.navigationlab.data.source.StudentLocalDataSource
import com.example.navigationlab.ui.detail.DetailViewModel
import com.example.navigationlab.ui.favorites.FavoritesViewModel
import com.example.navigationlab.ui.screen.AboutScreen
import com.example.navigationlab.ui.screen.DetailScreen
import com.example.navigationlab.ui.screen.FavoritesScreen
import com.example.navigationlab.ui.screen.HomeScreen
import com.example.navigationlab.ui.screen.ProfileScreen
import com.example.navigationlab.ui.home.HomeViewModel
import com.example.navigationlab.ui.home.HomeViewModelFactory

@Composable
fun NavigationLabApp() {
    val context = LocalContext.current

    val remoteDataSource = remember {
        StudentRemoteDataSource(RetrofitClient.apiService)
    }

    val dataSource = remember { remoteDataSource }
    val repository = remember { DefaultStudentRepository(dataSource) }
    val preferencesRepository = remember { UserPreferencesRepository(context) }
    val favoriteRepository = remember {
        FavoriteRepository(
            DatabaseProvider.getDatabase(context).favoriteStudentDao()
        )
    }

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(repository, preferencesRepository)
            )
            val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
            val searchQuery by homeViewModel.searchQuery.collectAsStateWithLifecycle()
            val isSortAscending by homeViewModel.isSortAscending.collectAsStateWithLifecycle()
            HomeScreen(
                uiState = uiState,
                searchQuery = searchQuery,
                isSortAscending = isSortAscending,
                onSearchQueryChange = homeViewModel::onSearchQueryChange,
                onStudentClick = { id -> navController.navigate(Routes.detail(id)) },
                onRetry = homeViewModel::refresh,
                onRefresh = homeViewModel::refresh,
                onOpenProfile = {
                    navController.navigate(Routes.PROFILE)
                },
                onOpenFavorites = {
                    navController.navigate(Routes.FAVORITES)
                },
                onOpenAbout = {
                    navController.navigate(Routes.ABOUT)
                },
                onToggleSort = homeViewModel::toggleSortAscending
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("studentId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: -1
            val viewModel: DetailViewModel = viewModel(
                factory = DetailViewModel.factory(repository, favoriteRepository)
            )
            LaunchedEffect(studentId) {
                viewModel.loadStudent(studentId)
            }
            DetailScreen(
                uiState = viewModel.uiState,
                onBack = { navController.popBackStack() },
                onToggleFavorite = viewModel::toggleFavorite
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.FAVORITES) {
            val favoritesViewModel: FavoritesViewModel = viewModel(
                factory = FavoritesViewModel.factory(favoriteRepository)
            )
            val favorites by favoritesViewModel.favorites.collectAsStateWithLifecycle()
            FavoritesScreen(
                favorites = favorites,
                onStudentClick = { id -> navController.navigate(Routes.detail(id)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ABOUT) {
            AboutScreen(
                onBack = { navController.popBackStack() })
        }
    }
}