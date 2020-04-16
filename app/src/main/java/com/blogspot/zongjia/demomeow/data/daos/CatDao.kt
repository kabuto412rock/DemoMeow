package com.blogspot.zongjia.demomeow.data.daos

import androidx.room.*
import com.blogspot.zongjia.demomeow.data.entities.Cat
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface CatDao {
    // 查詢所有在資料表的貓
    @Query("SELECT * FROM cats")
    fun getAllCats(): Observable<List<Cat>>

    // 查詢一隻id為 <catId>的貓
    @Query("SELECT * FROM cats WHERE id=:catId")
    fun getOne(catId: String): Flowable<List<Cat>>

    // 新增一隻貓到資料表
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(cat: Cat): Completable

    // 在資料表刪除一隻貓
    @Delete
    fun delete(cat: Cat): Completable

    //
    @Query("Delete FROM cats")
    fun deleteAll(): Completable

}