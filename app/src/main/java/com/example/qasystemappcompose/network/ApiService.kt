package com.example.qasystemappcompose.network

import com.example.qasystemappcompose.model.APIResponse
import okhttp3.Response
import retrofit2.http.GET

interface ApiService {
    @GET("v3/b/687374506063391d31aca23a")
    suspend fun getQuestions(): Response<APIResponse>
}