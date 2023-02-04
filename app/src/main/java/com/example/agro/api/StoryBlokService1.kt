package com.example.agro.api

import com.example.agro.model1.Content
import retrofit2.Response
import retrofit2.http.GET

interface StoryBlokService1 {
    @GET("cdn/stories/about2/shop?&token=SoAbU64xXFTDeRSw5FGEgQtt&version=published")
    suspend fun getStories(): Response<Content>
}