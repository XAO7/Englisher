package com.ao7.englisher

import com.ao7.englisher.data.WordType

fun getOriginType(origin: String): String {
	return if (origin.contains(" ") || origin.contains("-"))
		WordType.PHRASE.name
	else
		WordType.WORD.name
}