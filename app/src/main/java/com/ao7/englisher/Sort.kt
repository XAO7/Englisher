package com.ao7.englisher

import androidx.compose.ui.text.toLowerCase

enum class SortType {
	ORIGIN("origin"),
	ADDTIME("id");

	val columnName: String

	constructor(columnName: String) {
		this.columnName = columnName
	}
}

enum class SortOrder {
	ASCEND("ASC"),
	DESCEND("DESC");

	val sortKeyword: String

	constructor(sortKeyword: String) {
		this.sortKeyword = sortKeyword
	}
}

data class SortOptions(
	val sortType: SortType,
	val sortOrder: SortOrder
)