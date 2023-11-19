package com.ao7.englisher.ui.navigation

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ao7.englisher.AppEvent
import com.ao7.englisher.data.Word
import com.ao7.englisher.ui.viewmodel.BrowseViewModel

@Composable
fun BrowseScreen(
	browseViewModel: BrowseViewModel
) {
	val browseUiState = browseViewModel.browseUiState.collectAsState()
	var b by remember { browseViewModel.b }

	var translation by remember { mutableStateOf("") }

	Column(
	) {
		Surface {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.padding(10.dp)
			) {
				OutlinedTextField(
					value = b,
					onValueChange = {
						b = it
					},
					modifier = Modifier
						.weight(1f)
						.height(50.dp),
					keyboardOptions = KeyboardOptions.Default.copy(
						imeAction = ImeAction.Done
					)
				)
				Spacer(modifier = Modifier.width(10.dp))
				Button(
					onClick = { browseViewModel.onEvent(AppEvent.BrowseWord(b)) },
					shape = MaterialTheme.shapes.medium,
					contentPadding = PaddingValues(0.dp, 10.dp)
				) {
					Icon(imageVector = Icons.Filled.Search, contentDescription = null)
				}
			}
		}
		Card(
			modifier = Modifier.fillMaxSize()
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
						onValueChange = {
							translation = it
						},
						modifier = Modifier
							.weight(1f)
							.height(50.dp),
						keyboardOptions = KeyboardOptions.Default.copy(
							imeAction = ImeAction.Done
						)
					)
					Spacer(modifier = Modifier.width(10.dp))
					Button(
						onClick = {
							browseViewModel.onEvent(AppEvent.AddWord(Word(
								origin = b,
								phonic = browseUiState.value.browseResult.phonic,
								translation = translation
							)))
						},
						shape = MaterialTheme.shapes.medium,
						contentPadding = PaddingValues(0.dp, 10.dp)
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
				fontSize = 40.sp,
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