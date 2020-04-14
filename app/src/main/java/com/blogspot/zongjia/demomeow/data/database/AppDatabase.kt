package com.blogspot.zongjia.demomeow.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.blogspot.zongjia.demomeow.data.entities.Cat
import com.blogspot.zongjia.demomeow.data.daos.CatDao

@Database(entities = arrayOf(Cat::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun catDao(): CatDao
}