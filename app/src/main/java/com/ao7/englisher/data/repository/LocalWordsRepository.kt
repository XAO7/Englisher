package com.ao7.englisher.data.repository

import com.ao7.englisher.data.Word
import com.ao7.englisher.data.WordDao
import kotlinx.coroutines.flow.Flow

class LocalWordsRepository(private val wordDao: WordDao) : WordsRepository {

	override fun getAllWords(): Flow<List<Word>> = wordDao.getAllWords()

	override fun getWords(searchWord: String): Flow<List<Word>> = wordDao.getWords(searchWord)

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
}