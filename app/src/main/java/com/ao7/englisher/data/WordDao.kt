package com.ao7.englisher.data

import androidx.compose.ui.graphics.TransformOrigin
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

	@Query("SELECT * FROM Library ORDER BY origin")
	fun getAllWords(): Flow<List<Word>>

	@RawQuery(observedEntities = [Word::class])
	fun getWords(query: SupportSQLiteQuery): Flow<List<Word>>

	@Insert
	fun insertWord(word: Word)

	@Insert
	fun insertWords(wordList: List<Word>)

	@Query("DELETE FROM Library")
	fun deleteAllWords()

	@Delete
	fun deleteWord(word: Word)

	@Update
	fun updateWord(word: Word)

	@Query("SELECT COUNT(*) FROM Library WHERE origin = :origin")
	fun getWordCount(origin: String): Flow<Int>
}