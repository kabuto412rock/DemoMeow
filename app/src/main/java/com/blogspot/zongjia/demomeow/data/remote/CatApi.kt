package com.blogspot.zongjia.demomeow.data.remote

import com.blogspot.zongjia.demomeow.data.entities.Cat
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

// REST interface of Cat
interface CatApi {
//  取得Cat列表的API，limit是要求的Cat個數
    @GET("v1/images/search")

    fun getCats(@Query("limit") limit: Int)
    :Deferred<List<Cat>>
}