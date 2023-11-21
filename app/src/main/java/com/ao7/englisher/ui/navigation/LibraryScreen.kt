package com.ao7.englisher.ui.navigation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ao7.englisher.AppEvent
import com.ao7.englisher.data.SortOrder
import com.ao7.englisher.data.SortType
import com.ao7.englisher.data.Word
import com.ao7.englisher.ui.theme.Pink40
import com.ao7.englisher.ui.theme.Pink80
import com.ao7.englisher.ui.viewmodel.LibraryUiState
import com.ao7.englisher.ui.viewmodel.LibraryViewModel

@Composable
fun LibraryScreen(
	libraryViewModel: LibraryViewModel
) {
	val libraryUiState = libraryViewModel.libraryUiState.collectAsState()

	if (libraryUiState.value.isEditingWord) {
		EditWordDialog(libraryViewModel, libraryUiState)
	}

	if (libraryUiState.value.isChangingSort) {
		SortDialog(libraryViewModel, libraryUiState)
	}

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
					value = libraryUiState.value.searchWord,
					onValueChange = {
						libraryViewModel.searchWord.value = it
					},
					placeholder = { Text(text = "Search library", color = Color.Gray) },
					suffix = {
						Text(text = "Matched: " + libraryUiState.value.matchedCount, color = Color.Gray)
					},
					singleLine = true,
					modifier = Modifier
						.weight(1f),
					keyboardOptions = KeyboardOptions.Default.copy(
						imeAction = ImeAction.Done
					)
				)
				Spacer(modifier = Modifier.width(10.dp))
				Button(
					onClick = { libraryViewModel.onEvent(AppEvent.OpenSortDialog) },
					shape = MaterialTheme.shapes.medium,
					contentPadding = PaddingValues(0.dp, 10.dp)
				) {
					Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
				}
			}
		}
		LazyColumn() {
			itemsIndexed(libraryUiState.value.words) { index, it ->
				if (index == 0)
					Spacer(modifier = Modifier.height(8.dp))
				WordEntry(libraryViewModel, it)
			}
		}
	}
}

@Composable
fun WordEntry(
	libraryViewModel: LibraryViewModel,
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
			),
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth()
		) {
			Row(
				modifier = Modifier
					.weight(1f)
					.horizontalScroll(remember {
						ScrollState(0)
					})
			) {
				Text(
					text = word.origin,
					fontSize = 30.sp,
					modifier = Modifier.padding(20.dp, 5.dp)
				)
			}
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
				Row(
					modifier = Modifier
						.weight(1f)
						.horizontalScroll(remember {
							ScrollState(0)
						})
				) {
					Text(
						modifier = Modifier.padding(20.dp),
						text = word.phonic
					)
					Text(
						modifier = Modifier.padding(20.dp),
						text = word.translation
					)
				}
				IconButton(
					onClick = {
						libraryViewModel.onEvent(AppEvent.EditWord(word))
					},
					modifier = Modifier.padding(20.dp, 5.dp)
				) {
					Icon(
						imageVector = Icons.Filled.Edit,
						contentDescription = null
					)
				}
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWordDialog(
	libraryViewModel: LibraryViewModel,
	libraryUiState: State<LibraryUiState>
) {
	var editOrigin = remember {
		mutableStateOf(libraryUiState.value.editingWord.origin)
	}
	var editPhonic= remember {
		mutableStateOf(libraryUiState.value.editingWord.phonic)
	}
	var editTranslation = remember {
		mutableStateOf(libraryUiState.value.editingWord.translation)
	}

	AlertDialog(
		onDismissRequest = { libraryViewModel.onEvent(AppEvent.HideEditWordDialog) },
		modifier = Modifier
			.fillMaxWidth(0.95f)
	) {
		Card(
		) {
			Column(
				modifier = Modifier
					.padding(20.dp)
			) {
				Text(
					text = "Edit Word",
					fontSize = 30.sp,
					modifier = Modifier.padding(0.dp, 5.dp)
				)
				EditTextField(editOrigin, "Origin")
				EditTextField(editPhonic, "Phonic")
				EditTextField(editTranslation, "Translation")
				Spacer(modifier = Modifier.height(20.dp))
				Row(
					modifier = Modifier.fillMaxWidth()
				) {
					EditButton(
						text = "Delete",
						color = Pink40
					) {
						libraryViewModel.onEvent(AppEvent.DeleteEditingWord)
						libraryViewModel.onEvent(AppEvent.HideEditWordDialog)
					}
					EditButton(
						text = "Update",
						color = Pink80
					) {
						val word = libraryUiState.value.editingWord.copy(
							origin = editOrigin.value,
							phonic = editPhonic.value,
							translation = editTranslation.value
						)
						libraryViewModel.onEvent(AppEvent.UpdateEditingWord(word))
						libraryViewModel.onEvent(AppEvent.HideEditWordDialog)
					}
				}
			}
		}
	}
}

@Composable
fun EditTextField(
	editState: MutableState<String>,
	placeholder: String
) {
	TextField(
		modifier = Modifier.padding(0.dp, 5.dp),
		colors = TextFieldDefaults.colors(
			focusedContainerColor = Color.White,
		),
		placeholder = {
			Text(text = placeholder, color = Color.Gray)
		},
		singleLine = true,
		keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
		value = editState.value,
		onValueChange = { editState.value = it }
	)
}

@Composable
fun RowScope.EditButton(
	text: String,
	color: Color,
	onClick: () -> Unit
) {
	Button(
		modifier = Modifier
			.height(45.dp)
			.padding(5.dp, 0.dp)
			.weight(1f),
		onClick = onClick,
		colors = ButtonDefaults.buttonColors(
			containerColor = color
		),
	) {
		Row(
			modifier = Modifier.fillMaxSize(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Center
		) {
			Text(
				text = text,
				fontSize = 18.sp
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortDialog(
	libraryViewModel: LibraryViewModel,
	libraryUiState: State<LibraryUiState>
) {
	AlertDialog(
		onDismissRequest = { libraryViewModel.onEvent(AppEvent.HideSortDialog) },
		modifier = Modifier
			.fillMaxWidth(0.95f)
	) {
		Card(
		) {
			Column(
				modifier = Modifier
					.padding(20.dp),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center
			) {
				Row {
					enumValues<SortType>().forEach {
						SortRadioButton(
							text = it.name, selected = libraryUiState.value.sortOptions.sortType == it
						) {
							libraryViewModel.onEvent(
								AppEvent.ChangeSortOptions(libraryUiState.value.sortOptions.copy(
									sortType = it
								))
							)
						}
					}
				}
				Divider()
				Row {
					enumValues<SortOrder>().forEach {
						SortRadioButton(
							text = it.name, selected = libraryUiState.value.sortOptions.sortOrder == it
						) {
							libraryViewModel.onEvent(
								AppEvent.ChangeSortOptions(libraryUiState.value.sortOptions.copy(
									sortOrder = it
								))
							)
						}
					}
				}
			}
		}
	}
}

@Composable
fun SortRadioButton(
	text: String,
	selected: Boolean,
	onClick: () -> Unit
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier.padding(5.dp)
	) {
		Text(
			text = text
		)
		RadioButton(
			selected = selected,
			onClick = onClick
		)
	}
}