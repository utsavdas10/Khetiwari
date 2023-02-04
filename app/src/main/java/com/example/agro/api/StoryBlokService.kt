package com.example.agro.api

import com.example.agro.R
import com.example.agro.model1.Content
import com.example.agro.model1.ContentX
import retrofit2.Response
import retrofit2.http.GET

interface StoryBlokService {
    @GET("cdn/stories/about2/storage?&token=SoAbU64xXFTDeRSw5FGEgQtt&version=published")
    suspend fun getStories(): Response<Content>
}