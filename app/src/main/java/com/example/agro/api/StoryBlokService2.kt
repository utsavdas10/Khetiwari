package com.example.agro.api

import com.example.agro.model1.Content
import retrofit2.Response
import retrofit2.http.GET

interface StoryBlokService2 {
    @GET("cdn/stories/about2/mandi?&token=SoAbU64xXFTDeRSw5FGEgQtt&version=published")
    suspend fun getStories(): Response<Content>
}