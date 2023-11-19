package com.ao7.englisher.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ao7.englisher.AppEvent
import com.ao7.englisher.data.Word
import com.ao7.englisher.data.repository.TransRepository
import com.ao7.englisher.data.repository.WordsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

	private val _browseWord = MutableStateFlow("")

	@OptIn(ExperimentalCoroutinesApi::class)
	val browseUiState = _browseWord.flatMapLatest {
		transRepository.browse(it)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000L),
		initialValue = BrowseUiState()
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
	val phonic: String? = null,
	val translations: List<String> = listOf()
)