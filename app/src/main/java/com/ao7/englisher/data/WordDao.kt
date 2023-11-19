package com.ao7.englisher.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

	@Query("SELECT * FROM Library ORDER BY origin")
	fun getAllWords(): Flow<List<Word>>

	@Query("SELECT * FROM Library WHERE origin LIKE :searchWord ORDER BY origin")
	fun getWords(searchWord: String): Flow<List<Word>>

	@Insert
	fun insertWord(word: Word)

	@Insert
	fun insertWords(wordList: List<Word>)

	@Query("DELETE FROM Library")
	fun deleteAllWords()

	@Query("DELETE FROM Library WHERE origin = :origin")
	fun deleteWord(origin: String)
}