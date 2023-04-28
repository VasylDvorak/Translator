package com.translator.model.repository

import io.reactivex.rxjava3.core.Observable

interface Repository<T : Any> {

    fun getData(word: String): Observable<T>
}
