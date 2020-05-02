package com.sebbia.brzsmg.testtask


import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.sebbia.brzsmg.testtask.server.NewsApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val AppCompatActivity.app : Application
    get() = this.application as Application

val Fragment.app : Application
    get() = this.activity?.application as Application

/**
 * Обычно синглтон приложения.
 * Должен быть указан в манифесте.
 */
class Application : android.app.Application() {

    private lateinit var mNewsApi : NewsApi

    private var hsHttpClient: OkHttpClient? = null

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        hsHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .connectTimeout(4, TimeUnit.SECONDS)
            .readTimeout(4, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build()
        val retrofit = Retrofit.Builder()
            .client(hsHttpClient)
            .baseUrl("http://testtask.sebbia.com")
            .addConverterFactory(GsonConverterFactory.create(Json.gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        mNewsApi = retrofit.create(NewsApi::class.java)
    }

    val newsApi: NewsApi
        get() {
            return mNewsApi
        }
}