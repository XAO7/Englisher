package com.ao7.englisher.ui.navigation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ao7.englisher.AppEvent
import com.ao7.englisher.data.Word
import com.ao7.englisher.ui.viewmodel.LibraryViewModel

@Composable
fun LibraryScreen(
	libraryViewModel: LibraryViewModel
) {
	val libraryUiState = libraryViewModel.libraryUiState.collectAsState()
	var searchWord by remember { mutableStateOf("") }

	Column(
	) {
		Surface {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.padding(10.dp)
			) {
				OutlinedTextField(
					value = searchWord,
					onValueChange = {
						searchWord = it
						libraryViewModel.onEvent(AppEvent.SearchWordChanged(it))
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
					onClick = {  },
					shape = MaterialTheme.shapes.medium,
					contentPadding = PaddingValues(0.dp, 10.dp)
				) {
					Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
				}
			}
		}
		LazyColumn() {
			items(libraryUiState.value.words) {
				WordEntry(it)
//				Log.d("Library", it.origin)
			}
		}
	}
}

@Composable
fun WordEntry(
	word: Word
) {
	var expanded by remember { mutableStateOf(false) }

	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(5.dp)
			.animateContentSize(
				animationSpec = spring(
					dampingRatio = Spring.DampingRatioNoBouncy,
					stiffness = Spring.StiffnessMedium,
				)
			)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth()
		) {
			Text(
				text = word.origin,
				fontSize = 30.sp,
				modifier = Modifier.padding(20.dp, 5.dp)
			)
			Spacer(modifier = Modifier.weight(1f))
			IconButton(
				onClick = { expanded = !expanded },
				modifier = Modifier.padding(20.dp, 5.dp)
			) {
				Icon(
					imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
					contentDescription = null
				)
			}
		}
		if (expanded) {
			Row(
				modifier = Modifier.fillMaxWidth()
			) {
				Text(
					modifier = Modifier.padding(20.dp),
					text = word.translation
				)
			}
		}
	}
}