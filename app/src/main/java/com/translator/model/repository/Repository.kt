package com.translator.model.repository


interface Repository<T> {

    suspend fun getData(word: String): T
    suspend fun getFavoriteList(): T
}
