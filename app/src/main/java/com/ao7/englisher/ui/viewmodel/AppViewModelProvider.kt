package com.ao7.englisher.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ao7.englisher.EnglisherApplication

object AppViewModelProvider {
	val Factory = viewModelFactory {
		initializer {
			LibraryViewModel(
				englisherApplication().container.wordsRepository,
			)
		}

		initializer {
			BrowseViewModel(
				englisherApplication().container.transRepository,
				englisherApplication().container.wordsRepository
			)
		}
	}
}

fun CreationExtras.englisherApplication() : EnglisherApplication =
	this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EnglisherApplication