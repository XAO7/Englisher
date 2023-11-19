package com.ao7.englisher.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.ao7.englisher.ui.navigation.MainNav
import com.ao7.englisher.ui.theme.Purple80
import com.ao7.englisher.ui.theme.PurpleGrey40
import com.ao7.englisher.ui.viewmodel.BrowseViewModel
import com.ao7.englisher.ui.viewmodel.LibraryViewModel

@Composable
fun EnglisherApp(
	browseViewModel: BrowseViewModel,
	libraryViewModel: LibraryViewModel
) {
	val navController = rememberNavController()

	Scaffold(
		topBar = {
			EnglisherTopBar()
		},
		bottomBar = {
			EnglisherBottomBar(
				navigateTo = {
					navController.navigate(it) {
						popUpTo(0)
					}
				}
			)
		},
	) {
		Surface(
			modifier = Modifier.padding(it)
		) {
			MainNav(navController, browseViewModel, libraryViewModel)
		}
	}
}

@Composable
fun EnglisherTopBar() {
	Surface(
		color = Purple80,
		modifier = Modifier.fillMaxWidth()
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Center,
			modifier = Modifier.padding(0.dp, 10.dp)
		) {
			Text(
				text = "Englisher",
				fontSize = 30.sp
			)
		}
	}
}

@Composable
fun EnglisherBottomBar(
	navigateTo: (String) -> Unit
) {

	var currentRoute = remember { mutableStateOf("Browse") }

	Surface(
		modifier = Modifier.fillMaxWidth(),
		color = Purple80
	) {
		Row(
			modifier = Modifier
				.padding(30.dp, 10.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			BottomButton(item = bottomButtonItems[0], navigateTo, currentRoute)
			Spacer(modifier = Modifier.weight(1f))
			BottomButton(item = bottomButtonItems[1], navigateTo, currentRoute)
			Spacer(modifier = Modifier.weight(1f))
			BottomButton(item = bottomButtonItems[2], navigateTo, currentRoute)
		}
	}
}

@Composable
fun BottomButton(
	item: BottomButtonItem,
	navigateTo: (String) -> Unit,
	currentRoute: MutableState<String>
) {
	IconButton(onClick = {
		navigateTo(item.title)
		currentRoute.value = item.title
	}) {
		Icon(
			imageVector = item.icon,
			contentDescription = item.title,
			tint = if (currentRoute.value == item.title) Color.White else PurpleGrey40,
			modifier = Modifier.size(45.dp)
		)
	}
}

data class BottomButtonItem(
	val title: String,
	val icon: ImageVector,
)

val bottomButtonItems = listOf(
	BottomButtonItem("Browse", Icons.Filled.Search),
	BottomButtonItem("Library", Icons.Filled.List),
	BottomButtonItem("Settings", Icons.Filled.Settings),
)