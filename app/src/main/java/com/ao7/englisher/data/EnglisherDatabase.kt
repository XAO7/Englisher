package com.ao7.englisher.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class EnglisherDatabase : RoomDatabase() {

	abstract fun wordDao(): WordDao

	companion object {
		@Volatile
		private var Instance: EnglisherDatabase? = null

		fun getDatabase(context: Context) : EnglisherDatabase{
			Log.d("ao", "getDatabase called")

			return Instance ?: synchronized(this) {
				Room.databaseBuilder(context, EnglisherDatabase::class.java, "word_database")
					.build()
					.also { Instance = it }
			}
		}
	}
}