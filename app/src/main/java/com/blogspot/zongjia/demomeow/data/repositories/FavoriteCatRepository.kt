package com.blogspot.zongjia.demomeow.data.repositories

import com.blogspot.zongjia.demomeow.data.daos.CatDao
import com.blogspot.zongjia.demomeow.data.entities.Cat
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface IFavoriteCatRepository {

    fun getOne(catId: String): Flowable<List<Cat>>

    fun getAll(): Observable<List<Cat>>

    fun insert(cat: Cat): Completable

    fun delete(cat: Cat): Completable

    fun deleteAll(): Completable
}

class FavoriteCatRepository(private val catDao: CatDao) : IFavoriteCatRepository {
    override fun getOne(catId: String): Flowable<List<Cat>> = catDao.getOne(catId)

    override fun getAll(): Observable<List<Cat>> = catDao.getAllCats()

    override fun insert(cat: Cat): Completable {
        return catDao.insert(cat)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }

    override fun delete(cat: Cat): Completable {
        return catDao.delete(cat)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun deleteAll(): Completable {
        return catDao.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
