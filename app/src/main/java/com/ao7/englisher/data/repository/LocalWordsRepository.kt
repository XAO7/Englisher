package com.ao7.englisher.data.repository

import androidx.sqlite.db.SupportSQLiteQuery
import com.ao7.englisher.data.Word
import com.ao7.englisher.data.WordDao
import kotlinx.coroutines.flow.Flow

class LocalWordsRepository(private val wordDao: WordDao) : WordsRepository {

	override fun getAllWords(): Flow<List<Word>> = wordDao.getAllWords()

	override fun getWords(query: SupportSQLiteQuery): Flow<List<Word>> = wordDao.getWords(query)

	override fun insertWord(word: Word) {
		wordDao.insertWord(word)
	}

	override fun insertWords(wordList: List<Word>) {
		wordDao.insertWords(wordList)
	}

	override fun deleteAllWords() {
		wordDao.deleteAllWords()
	}

	override fun deleteWord(word: Word) {
		wordDao.deleteWord(word)
	}

	override fun updateWord(word: Word) {
		wordDao.updateWord(word)
	}

	override fun getWordCount(origin: String): Flow<Int> {
		return wordDao.getWordCount(origin)
	}
}