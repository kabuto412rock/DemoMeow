package com.blogspot.zongjia.demomeow.module

import com.blogspot.zongjia.demomeow.data.remote.CatApi
import com.blogspot.zongjia.demomeow.data.repositories.CatRepository
import com.blogspot.zongjia.demomeow.data.repositories.CatRepositoryImpl
import com.blogspot.zongjia.demomeow.presentation.main.MainViewModel
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val CAT_API_BASE_URL = "https://thecatapi.com/v1/"

var appModules = module {
    // The retrofit service using our custom HTTP client instance as a singleton
    single{
        createWebService<CatApi>(
            okHttpClient = createHttpClient(),
            factory = RxJava2CallAdapterFactory.create(),
            baseUrl = CAT_API_BASE_URL
        )
    }
    // Tell koin how to create an instance of CatRepository
    factory<CatRepository> {
        CatRepositoryImpl(catApi = get())
    }

    // Specific viewModel pattern to tell Koin how to build MainViewModel
    viewModel { MainViewModel(catRepository = get()) }

}
/* Returns a custom OkHttpClient instance with interceptor. Used for building Retrofit service */
fun createHttpClient (): OkHttpClient {
    val client = OkHttpClient.Builder()
    client.readTimeout(5 * 60, TimeUnit.SECONDS)
    return client.addInterceptor {
        val original = it.request()
        val requestBuilder = original.newBuilder()
        requestBuilder.header("Content-Type", "application/json")
        val request = requestBuilder.method(original.method(), original.body()).build()
        return@addInterceptor it.proceed(request)
    }.build()
}
/* function to build our Retrofit service */
inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient,
    factory: CallAdapter.Factory, baseUrl: String
): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addCallAdapterFactory(factory)
        .client(okHttpClient)
        .build()
    return retrofit.create(T::class.java)
}