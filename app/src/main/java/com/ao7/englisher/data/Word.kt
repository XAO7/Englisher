package com.ao7.englisher.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Library")
data class Word(
	@PrimaryKey(autoGenerate = true)
	val id: Int? = null,
	val origin: String = "",
	val translation: String = "",
	val pronunciation: String = ""
)