package com.blogspot.zongjia.demomeow.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.blogspot.zongjia.demomeow.data.entities.Cat

@Dao
interface CatDao {
    @Query("SELECT * FROM cat")
    fun getAll(): List<Cat>

    @Insert
    fun insertOne(cat: Cat)

    @Delete
    fun delete(cat: Cat)
}