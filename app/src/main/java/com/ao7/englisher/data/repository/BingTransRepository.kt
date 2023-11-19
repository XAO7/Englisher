package com.ao7.englisher.data.repository

import android.util.Log
import com.ao7.englisher.network.BingTransService
import com.ao7.englisher.ui.viewmodel.BrowseUiState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BingTransRepository(val bingTransService: BingTransService) : TransRepository {

	override fun browse(text: String): Flow<BrowseUiState> = callbackFlow {
		if (text != "") {
			bingTransService.translate(text).enqueue(
				object : Callback<String> {
					override fun onResponse(call: Call<String>, response: Response<String>) {
						val htmlString = response.body().toString()
						var translations = mutableListOf<String>()
						val document = Jsoup.parse(htmlString)

						val phonic = document.selectFirst("body > div.contentPadding > div > div > div.lf_area > div.qdef > div.hd_area > div.hd_tf_lh > div > div.hd_prUS.b_primtxt")
							?.text()

						repeat(8) {
							val element = document
								.selectFirst("body > div.contentPadding > div > div > div.lf_area > div.qdef > ul > li:nth-child($it) > span.def.b_regtxt > span")

							if (element != null) {
								translations.add(element.text())
								Log.d("DDD", element.text())
							}
						}

						trySend(BrowseUiState(phonic, translations))
					}

					override fun onFailure(call: Call<String>, t: Throwable) {
						println("FAIL")
					}
				}
			)
		}

		awaitClose {}
	}
}