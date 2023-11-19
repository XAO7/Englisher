package com.ao7.englisher.data.repository

import com.ao7.englisher.ui.viewmodel.BrowseResult
import com.ao7.englisher.ui.viewmodel.BrowseUiState
import kotlinx.coroutines.flow.Flow

interface TransRepository {
	fun browse(text: String): Flow<BrowseResult>
}