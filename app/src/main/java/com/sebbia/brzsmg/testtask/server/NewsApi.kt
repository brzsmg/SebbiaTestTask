package com.sebbia.brzsmg.testtask.server

import com.sebbia.brzsmg.testtask.model.Category
import com.sebbia.brzsmg.testtask.model.News
import com.sebbia.brzsmg.testtask.model.NewsEntity
import com.sebbia.brzsmg.testtask.model.ResultEntity
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API Новостей.
 */
interface NewsApi {

    @GET("/v1/news/categories")
    fun requestCategories() : Single<Response<ResultEntity<Category>>>

    @GET("/v1/news/categories/{id}/news")
    fun requestCategory(
        @Path("id") id : Int,
        @Query("page") page : Int
    ) : Single<Response<ResultEntity<News>>>

    @GET("/v1/news/details")
    fun requestNewsDetails(@Query("id") id : Int) : Single<Response<NewsEntity>>

}
