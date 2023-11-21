package com.ao7.englisher.data.repository

import androidx.sqlite.db.SupportSQLiteQuery
import com.ao7.englisher.data.Word
import kotlinx.coroutines.flow.Flow

interface WordsRepository {
	fun getAllWords(): Flow<List<Word>>
	fun getWords(query: SupportSQLiteQuery): Flow<List<Word>>
	fun insertWord(word: Word)
	fun insertWords(wordList: List<Word>)
	fun deleteAllWords()
	fun deleteWord(word: Word)
	fun updateWord(word: Word)
	fun getWordCount(origin: String): Flow<Int>
}