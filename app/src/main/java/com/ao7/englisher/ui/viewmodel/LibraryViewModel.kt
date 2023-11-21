package com.ao7.englisher.ui.viewmodel

import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.ao7.englisher.AppEvent
import com.ao7.englisher.SortOptions
import com.ao7.englisher.SortOrder
import com.ao7.englisher.SortType
import com.ao7.englisher.data.Word
import com.ao7.englisher.data.repository.WordsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LibraryViewModel(
	private val wordsRepository: WordsRepository,
) : ViewModel() {

	private val _libraryUiState = MutableStateFlow(LibraryUiState())
	val searchWord = MutableStateFlow("")

	private val _words = combine(_libraryUiState, searchWord){_, _ ->}.flatMapLatest {
		wordsRepository.getWords(SimpleSQLiteQuery("SELECT * FROM Library WHERE origin LIKE \"%${searchWord.value}%\" OR translation LIKE \"%${searchWord.value}%\" ORDER BY ${_libraryUiState.value.sortOptions.sortType.columnName} ${_libraryUiState.value.sortOptions.sortOrder.sortKeyword}"))
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000L),
		initialValue = emptyList()
	)

	val libraryUiState = combine(_libraryUiState, searchWord, _words) { state, searchWord, words ->
		state.copy(
			searchWord = searchWord,
			words = words,
			matchedCount = words.size
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000L),
		initialValue = LibraryUiState()
	)

	suspend fun getAllWords(): List<Word> = runBlocking {
		var result = emptyList<Word>()

		result = wordsRepository.getAllWords().first()
		Log.d("DDD", result.size.toString())

		result
	}

	fun insertWord(word: Word) {
		GlobalScope.launch(Dispatchers.IO) {
			wordsRepository.insertWord(word)
		}
	}

	fun insertWords(wordList: List<Word>) {
		GlobalScope.launch(Dispatchers.IO) {
			wordsRepository.insertWords(wordList)
		}
	}

	fun deleteAllWords() {
		GlobalScope.launch(Dispatchers.IO) {
			wordsRepository.deleteAllWords()
		}
	}

	fun deleteWord(word: Word) {
		GlobalScope.launch(Dispatchers.IO) {
			wordsRepository.deleteWord(word)
		}
	}

	fun updateWord(word: Word) {
		GlobalScope.launch(Dispatchers.IO) {
			wordsRepository.updateWord(word)
		}
	}

	fun onEvent(event: AppEvent) {
		when (event) {
			is AppEvent.EditWord -> {
				_libraryUiState.update {
					it.copy(
						isEditingWord = true,
						editingWord = event.word
					)
				}
			}
			is AppEvent.HideEditWordDialog -> {
				_libraryUiState.update { it.copy(isEditingWord = false) }
			}
			is AppEvent.DeleteEditingWord -> {
				deleteWord(libraryUiState.value.editingWord)
			}
			is AppEvent.UpdateEditingWord -> {
				updateWord(event.word)
			}

			is AppEvent.OpenSortDialog -> {
				_libraryUiState.update { it.copy(isChangingSort = true) }
			}
			is AppEvent.HideSortDialog -> {
				_libraryUiState.update { it.copy(isChangingSort = false) }
			}
			is AppEvent.ChangeSortOptions -> {
				_libraryUiState.update { it.copy(sortOptions = event.sortOptions) }
			}

			is AppEvent.ImportTxt -> {
				deleteAllWords()
				openFile(startForImportResult, "/sdcard/Documents".toUri())
				GlobalScope.launch(Dispatchers.IO) {
					wordListImport.clear()
					while (!wordListImportReady) {
						delay(500L)
					}
					insertWords(wordListImport)
					wordListImportReady = false
				}
			}
			is AppEvent.ExportTxt -> {
				createFile(startForExportResult, "/sdcard/Documents".toUri())
			}
		}
	}

	lateinit var startForImportResult: ActivityResultLauncher<Intent>
	lateinit var wordListImport: MutableList<Word>
	var wordListImportReady: Boolean = false
	lateinit var startForExportResult: ActivityResultLauncher<Intent>

	fun openFile(startForImportResult: ActivityResultLauncher<Intent>, pickerInitalUri: Uri) {
		val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
			addCategory(Intent.CATEGORY_OPENABLE)
			type = "text/plain"
			putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitalUri)
		}
		startForImportResult.launch(intent)
	}

	fun createFile(startForExportResult: ActivityResultLauncher<Intent>, initalUri: Uri) {
		val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
			addCategory(Intent.CATEGORY_DEFAULT)
			type = "text/plain"
			putExtra(DocumentsContract.EXTRA_INITIAL_URI, initalUri)
		}

		startForExportResult.launch(Intent.createChooser(intent, "Choose Directory"))
	}
}

data class LibraryUiState(
	val words: List<Word> = listOf(),
	val searchWord: String = "",
	val isEditingWord: Boolean = false,
	val editingWord: Word = Word(),
	val matchedCount: Int = 0,

	val isChangingSort: Boolean = false,
	val sortOptions: SortOptions = SortOptions(SortType.ORIGIN, SortOrder.ASCEND),
)