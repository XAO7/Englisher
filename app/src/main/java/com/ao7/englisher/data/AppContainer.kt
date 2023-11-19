package com.ao7.englisher.data

import android.content.Context
import com.ao7.englisher.data.repository.BingTransRepository
import com.ao7.englisher.data.repository.LocalWordsRepository
import com.ao7.englisher.data.repository.TransRepository
import com.ao7.englisher.data.repository.WordsRepository
import com.ao7.englisher.network.BingTransService
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

interface AppContainer {
	val wordsRepository: WordsRepository
	val transRepository: TransRepository
}

class EnglisherAppContainer(private val context: Context) : AppContainer {

	val bingTransService by lazy {
		Retrofit
			.Builder()
			.addConverterFactory(ScalarsConverterFactory.create())
			.baseUrl("https://www.bing.com/")
			.build()
			.create(BingTransService::class.java)
	}

	override val transRepository: TransRepository by lazy {
		BingTransRepository(bingTransService)
	}

	override val wordsRepository: WordsRepository by lazy {
		LocalWordsRepository(EnglisherDatabase.getDatabase(context).wordDao())
	}
}