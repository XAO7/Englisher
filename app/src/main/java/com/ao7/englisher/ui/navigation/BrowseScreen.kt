package com.ao7.englisher.ui.navigation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ao7.englisher.AppEvent
import com.ao7.englisher.data.Word
import com.ao7.englisher.getOriginType
import com.ao7.englisher.ui.theme.Pink40
import com.ao7.englisher.ui.viewmodel.BrowseViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BrowseScreen(
	browseViewModel: BrowseViewModel
) {
	val browseUiState = browseViewModel.browseUiState.collectAsState()
	var b by remember { browseViewModel.b }

	var translation by remember { mutableStateOf("") }

	val controller = LocalSoftwareKeyboardController.current

	Column(
	) {
		Surface(
			shadowElevation = 5.dp
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.padding(10.dp)
			) {
				OutlinedTextField(
					value = b,
					onValueChange = {
						b = it
					},
					placeholder = { Text(text = "Search word", color = Color.Gray) },
					modifier = Modifier
						.weight(1f),
					keyboardOptions = KeyboardOptions.Default.copy(
						imeAction = ImeAction.Done
					),
					singleLine = true
				)
				Spacer(modifier = Modifier.width(10.dp))
				Button(
					onClick = {
						browseViewModel.onEvent(AppEvent.BrowseWord(b))
						controller?.hide()
						translation = ""
					},
					shape = MaterialTheme.shapes.medium,
					contentPadding = PaddingValues(0.dp, 10.dp)
				) {
					Icon(imageVector = Icons.Filled.Search, contentDescription = null)
				}
			}
		}

		if (browseUiState.value.browseResult.success) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(65.dp),
				contentAlignment = Alignment.Center
			) {
				Text(
					text = browseUiState.value.browseResult.origin,
					fontSize = 40.sp,
					fontWeight = FontWeight.Bold
				)
			}
			if (!browseUiState.value.canAddWord) {
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.height(30.dp)
						.background(color = Pink40),
					contentAlignment = Alignment.Center,
				) {
					Text(
						text = "Already in library",
						fontSize = 20.sp,
						fontWeight = FontWeight.Bold,
						color = Color.White
					)
				}
			}
		}

		Card(
			modifier = Modifier
				.fillMaxSize()
				.animateContentSize(
					animationSpec = spring(
						dampingRatio = Spring.DampingRatioNoBouncy,
						stiffness = Spring.StiffnessMedium,
					)
				)
		) {
			Column {
				if (browseUiState.value.browseResult.phonic != "") {
					Phonic(phonicText = browseUiState.value.browseResult.phonic)
				}
				browseUiState.value.browseResult.translations.forEach {
					Translation(transText = it)
				}

				Spacer(modifier = Modifier.weight(1f))
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.padding(30.dp, 10.dp),
				) {
					OutlinedTextField(
						value = translation,
						placeholder = { Text(text = "Type translation", color = Color.Gray)},
						onValueChange = {
							translation = it
						},
						modifier = Modifier
							.weight(1f),
						keyboardOptions = KeyboardOptions.Default.copy(
							imeAction = ImeAction.Done
						),
						singleLine = true
					)
					Spacer(modifier = Modifier.width(10.dp))
					Button(
						onClick = {
							browseViewModel.onEvent(AppEvent.AddWord(Word(
								origin = browseUiState.value.browseResult.origin,
								phonic = browseUiState.value.browseResult.phonic,
								translation = translation,
								language = "EN",
								type = getOriginType(browseUiState.value.browseResult.origin),
								addTime = System.currentTimeMillis()
							)))
							controller?.hide()
							translation = ""
						},
						shape = MaterialTheme.shapes.medium,
						contentPadding = PaddingValues(0.dp, 10.dp),
						enabled = browseUiState.value.canAddWord && translation.isNotEmpty()
					) {
						Icon(imageVector = Icons.Filled.Add, contentDescription = null)
					}
				}
			}
		}
	}
}

@Composable
fun Phonic(
	phonicText: String
) {
	Card(
		modifier = Modifier
			.padding(10.dp),
		colors = CardDefaults.cardColors(containerColor = Color.White),
		elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = phonicText,
				modifier = Modifier.padding(20.dp, 0.dp),
				fontSize = 30.sp,
				fontWeight = FontWeight.Bold
			)
		}
	}
}

@Composable
fun Translation(
	transText: String
) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(10.dp),
		colors = CardDefaults.cardColors(containerColor = Color.White),
		elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = transText,
				modifier = Modifier.padding(20.dp, 0.dp),
				fontSize = 20.sp
			)
		}
	}
}