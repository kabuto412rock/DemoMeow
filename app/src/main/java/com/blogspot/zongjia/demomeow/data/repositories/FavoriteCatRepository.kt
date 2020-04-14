package com.blogspot.zongjia.demomeow.data.repositories

import com.blogspot.zongjia.demomeow.data.entities.Cat
import com.blogspot.zongjia.demomeow.util.UseCaseResult

interface FavoriteCatRepository {
    // Suspend is used to await the result from Deferred
    suspend fun getCatList(): UseCaseResult<List<Cat>>
}