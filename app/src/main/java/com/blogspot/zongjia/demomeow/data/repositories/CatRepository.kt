package com.blogspot.zongjia.demomeow.data.repositories

import com.blogspot.zongjia.demomeow.data.entities.Cat
import com.blogspot.zongjia.demomeow.data.remote.CatApi
import com.blogspot.zongjia.demomeow.util.UseCaseResult
import java.lang.Exception

// 我們想要的貓咪數量
const val NUMBERS_OF_CAT = 25
interface CatRepository {
    // Suspend is used to await the result from Deferred
    suspend fun getCatList(): UseCaseResult<List<Cat>>
}

class CatRepositoryImpl(private val catApi: CatApi):
    CatRepository {
    override suspend fun getCatList(): UseCaseResult<List<Cat>> {
        /*
        * We try to return a list of cats from the API
        * Await the result from web service and then return it, catching any error from API
        * */
        return try {
            val result = catApi.getCats(limit= NUMBERS_OF_CAT).await()
            UseCaseResult.Success(result)
        }catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }
}