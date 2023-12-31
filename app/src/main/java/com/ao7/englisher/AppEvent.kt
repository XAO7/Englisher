package com.ao7.englisher

import com.ao7.englisher.data.SortOptions
import com.ao7.englisher.data.Word

interface AppEvent {
	data class BrowseWord(val browseWord: String): AppEvent
	data class AddWord(val addWord: Word): AppEvent

	data class EditWord(val word: Word): AppEvent
	object DeleteEditingWord: AppEvent
	data class UpdateEditingWord(val word: Word): AppEvent
	object HideEditWordDialog: AppEvent

	object OpenSortDialog: AppEvent
	object HideSortDialog: AppEvent
	data class ChangeSortOptions(val sortOptions: SortOptions): AppEvent

	object ImportTxt : AppEvent
	object ExportTxt : AppEvent
}