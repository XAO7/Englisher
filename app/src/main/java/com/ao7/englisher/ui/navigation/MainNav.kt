package com.ao7.englisher.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ao7.englisher.ui.viewmodel.BrowseViewModel
import com.ao7.englisher.ui.viewmodel.LibraryViewModel

@Composable
fun MainNav(
	navController: NavHostController,
	browseViewModel: BrowseViewModel,
	libraryViewModel: LibraryViewModel
) {
	NavHost(navController = navController, startDestination = "Browse",) {
		composable("Browse") {
			BrowseScreen(browseViewModel)
		}
		composable("Library") {
			LibraryScreen(libraryViewModel)
		}
		composable("Settings") {
			SettingsScreen(libraryViewModel)
		}
	}
}