package com.ao7.englisher.ui.viewmodel

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

	val browseUiState = combine(_browseUiState, _browseResult, _browseWord) { state, result, word ->
		state.copy(
			browseResult = result,
			browseWord = word
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
	val browseWord: String = ""
)

data class BrowseResult(
	val phonic: String = "",
	val translations: List<String> = listOf()
)