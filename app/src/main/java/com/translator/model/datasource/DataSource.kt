package com.translator.model.datasource

import io.reactivex.rxjava3.core.Observable


interface DataSource<T : Any> {

    fun getData(word: String): Observable<T>
}
