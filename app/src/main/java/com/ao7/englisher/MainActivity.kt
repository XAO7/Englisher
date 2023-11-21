package com.ao7.englisher

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ao7.englisher.data.Word
import com.ao7.englisher.data.WordType
import com.ao7.englisher.ui.viewmodel.LibraryViewModel
import com.ao7.englisher.ui.viewmodel.AppViewModelProvider
import com.ao7.englisher.ui.viewmodel.BrowseViewModel
import com.ao7.englisher.ui.EnglisherApp
import com.ao7.englisher.ui.theme.EnglisherTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {

	var wordListImport = mutableListOf<Word>()

	lateinit var libraryViewModel: LibraryViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			EnglisherTheme {
				libraryViewModel = viewModel(factory = AppViewModelProvider.Factory)
				val browseViewModel: BrowseViewModel = viewModel(factory = AppViewModelProvider.Factory)

				libraryViewModel.startForImportResult = startForImportResult
				libraryViewModel.wordListImport = wordListImport

				libraryViewModel.startForExportResult = startForExportResult

				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					EnglisherApp(browseViewModel, libraryViewModel)
				}
			}
		}
	}

	private val startForImportResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
			result: ActivityResult ->
		if (result.resultCode == Activity.RESULT_OK) {
			val intent = result.data
			if (intent?.data != null) {
				linesToWords(readLinesFromUri(intent.data!!), wordListImport)
				libraryViewModel.wordListImportReady = true
				Log.d("AO7DEBUG", "Words Found: " + wordListImport.size.toString())
			}
		}
	}

	private val startForExportResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
			result: ActivityResult ->
		if (result.resultCode == Activity.RESULT_OK) {
			val intent = result.data
			if (intent?.data != null) {
				GlobalScope.launch {
					alterDocument(intent.data!!, wordsToString(libraryViewModel.getAllWords()))
				}
			}
		}
	}

	private val _contentResolver by lazy { applicationContext.contentResolver }

	private fun readLinesFromUri(uri: Uri): List<String> {
		val lines = mutableListOf<String>()
		_contentResolver.openInputStream(uri)?.use {
			BufferedReader(InputStreamReader(it)).use { reader ->
				var line: String? = reader.readLine()
				while (line != null) {
					if (line != "")
						lines.add(line)
					line = reader.readLine()
				}
			}
		}
		return lines
	}

	private fun linesToWords(lines: List<String>, wordList: MutableList<Word>) {
		lines.forEach {
			val parts = it.split("/")
			wordList.add(
				Word(
					origin = parts[0],
					phonic = if (parts.size > 1) parts[1] else "",
					translation = if (parts.size > 2) parts[2] else "",
					language = if (parts.size > 3 && parts[3] != "") parts[3] else "EN",
					type = if (parts.size > 4 && parts[4] != "") parts[4] else getOriginType(parts[0]),
					addTime = if (parts.size > 5 && parts[5] != "") parts[5].toLong() else System.currentTimeMillis(),
				)
			)
		}
	}

	private fun wordsToString(words: List<Word>): String {
		var string = ""
		words.forEach {
			string += it.origin + "/" + it.phonic + "/" + it.translation + "/" + it.type + "/" + it.language + "/" + it.addTime + "\n"
		}
		return string
	}

	private fun alterDocument(uri: Uri, string: String) {
		try {
			contentResolver.openFileDescriptor(uri, "w")?.use {
				FileOutputStream(it.fileDescriptor).use { stream ->
					stream.write(
						string.toByteArray()
					)
				}
			}
		} catch (e: FileNotFoundException) {
			e.printStackTrace()
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}
}