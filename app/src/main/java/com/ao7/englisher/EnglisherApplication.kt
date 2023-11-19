package com.ao7.englisher

import android.app.Application
import com.ao7.englisher.data.AppContainer
import com.ao7.englisher.data.EnglisherAppContainer

class EnglisherApplication : Application() {

	lateinit var container: AppContainer

	override fun onCreate() {
		super.onCreate()

		container = EnglisherAppContainer(this)
	}
}