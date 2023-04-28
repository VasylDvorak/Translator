package com.translator.model.repository


interface Repository<T : Any> {

   suspend fun getData(word: String): T
}
