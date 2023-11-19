package com.ao7.englisher.data.repository

import com.ao7.englisher.data.Word
import kotlinx.coroutines.flow.Flow

interface WordsRepository {
	fun getAllWords(): Flow<List<Word>>
	fun getWords(searchWord: String): Flow<List<Word>>
	fun insertWord(word: Word)
	fun insertWords(wordList: List<Word>)
	fun deleteAllWords()
	fun deleteWord(origin: String)
}