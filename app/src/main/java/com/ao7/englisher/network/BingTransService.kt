package com.ao7.englisher.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface BingTransService {
	@GET("/dict/search")
	fun translate(
		@Query("q") text: String,
		@Header("User-Agent") userAgent: String = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 Edg/119.0.0.0",
		@Header("Sec-Fetch-Site") fetchSite: String = "same-origin",
		@Header("Referer") referer: String = "https://www.bing.com/search?q=what&qs=n&form=QBLH&sp=-1&lq=0&pq=what&sc=10-4&sk=&cvid=02C8782498E940B5A7EEF40E8888F043&ghsh=0&ghacc=0&ghpl=",
		@Header("Cookie") cookie: String = "MUID=303D9D342CEA641C30548EBA2DE265F4; MUIDB=303D9D342CEA641C30548EBA2DE265F4; ipv6=hit=1700293318512&t=4; MicrosoftApplicationsTelemetryDeviceId=8bf8306b-e480-4e4d-bc19-828af077c10c; ai_session=wQnBXgqoMClS6zcYcyRjXa|1700289719955|1700289730285; _HPVN=CS=eyJQbiI6eyJDbiI6MSwiU3QiOjAsIlFzIjowLCJQcm9kIjoiUCJ9LCJTYyI6eyJDbiI6MSwiU3QiOjAsIlFzIjowLCJQcm9kIjoiSCJ9LCJReiI6eyJDbiI6MSwiU3QiOjAsIlFzIjowLCJQcm9kIjoiVCJ9LCJBcCI6dHJ1ZSwiTXV0ZSI6dHJ1ZSwiTGFkIjoiMjAyMy0xMS0xOFQwMDowMDowMFoiLCJJb3RkIjowLCJHd2IiOjAsIlRucyI6MCwiRGZ0IjpudWxsLCJNdnMiOjAsIkZsdCI6MCwiSW1wIjo1LCJUb2JicyI6MH0=; _UR=HP=&QS=0&TQS=0; _SS=SID=089825BA01AD693423893677007468F5&R=83&RB=83&GB=0&RG=0&RP=83; _EDGE_S=F=1&SID=089825BA01AD693423893677007468F5&mkt=zh-cn;"
	): Call<String>
}