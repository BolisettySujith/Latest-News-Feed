package com.example.newsfeed

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApi {
    @GET("/v2/top-headlines?country=in&category=Technology")
    suspend fun getTechnologyNews(@Query("apiKey") apiKey: String) : Response<NewsApiResponse>

    @GET("/v2/top-headlines?country=in&category=Business")
    suspend fun getBusinessNews(@Query("apiKey") apiKey: String) : Response<NewsApiResponse>

    @GET("/v2/top-headlines?country=in&category=Entertainment")
    suspend fun getEntertainmentNews(@Query("apiKey") apiKey: String) : Response<NewsApiResponse>

    @GET("/v2/top-headlines?country=in&category=General")
    suspend fun getGeneralNews(@Query("apiKey") apiKey: String) : Response<NewsApiResponse>

    @GET("/v2/top-headlines?country=in&category=Health")
    suspend fun getHealthNews(@Query("apiKey") apiKey: String) : Response<NewsApiResponse>

    @GET("/v2/top-headlines?country=in&category=Science")
    suspend fun getScienceNews(@Query("apiKey") apiKey: String) : Response<NewsApiResponse>

    @GET("/v2/top-headlines?country=in&category=Sports")
    suspend fun getSportsNews(@Query("apiKey") apiKey: String) : Response<NewsApiResponse>
}