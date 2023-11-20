package com.ao7.englisher.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ao7.englisher.AppEvent
import com.ao7.englisher.data.Word
import com.ao7.englisher.data.repository.TransRepository
import com.ao7.englisher.data.repository.WordsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BrowseViewModel(
	private val transRepository: TransRepository,
	private val wordsRepository: WordsRepository
) : ViewModel() {

	var b = mutableStateOf("")

	private val _browseWord = MutableStateFlow("")
	private val _browseUiState = MutableStateFlow(BrowseUiState(browseResult = BrowseResult()))
	private val _browseResult = _browseWord.flatMapLatest {
		transRepository.browse(it)
	}
	private val _browseWordInLibrary = _browseResult.flatMapLatest {
		wordsRepository.getWordCount(it.origin).map { count ->
			count > 0
		}
	}

	val browseUiState = combine(_browseUiState, _browseResult, _browseWord, _browseWordInLibrary) { state, result, word, inLibrary ->
		state.copy(
			browseResult = result,
			browseWord = word,
			canAddWord = !inLibrary
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000L),
		initialValue = BrowseUiState(
			browseResult = BrowseResult()
		)
	)

	fun insertWord(word: Word) {
		GlobalScope.launch(Dispatchers.IO) {
			wordsRepository.insertWord(word)
		}
	}

	fun onEvent(event: AppEvent) {
		when(event) {
			is AppEvent.BrowseWord -> {
				_browseWord.value = event.browseWord
			}
			is AppEvent.AddWord -> {
				insertWord(event.addWord)
			}
		}
	}
}

data class BrowseUiState(
	val browseResult: BrowseResult,
	val browseWord: String = "",
	val canAddWord: Boolean = false
)

data class BrowseResult(
	val success: Boolean = false,
	val origin: String = "",
	val phonic: String = "",
	val translations: List<String> = listOf()
)