package com.ao7.englisher.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ao7.englisher.AppEvent
import com.ao7.englisher.ui.viewmodel.LibraryViewModel

@Composable
fun SettingsScreen(
	libraryViewModel: LibraryViewModel
) {
	Column {
		SettingEntry("Import from .txt", Icons.Filled.AddCircle) {
			libraryViewModel.onEvent(AppEvent.ImportTxt)
		}
		SettingEntry("Export as .txt", Icons.Filled.ExitToApp) {
			libraryViewModel.onEvent(AppEvent.ExportTxt)
		}
	}
}

@Composable
fun SettingEntry(
	text: String,
	icon: ImageVector,
	onClick: () -> Unit
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.clickable(enabled = true, onClick = onClick),
		verticalAlignment = Alignment.CenterVertically
	) {
		Spacer(modifier = Modifier.width(20.dp))
		Icon(
			imageVector = icon,
			contentDescription = null,
			modifier = Modifier.size(30.dp)
		)
		Text(
			text = text,
			modifier = Modifier
				.padding(15.dp),
			fontSize = 25.sp
		)
	}
	Divider()
}