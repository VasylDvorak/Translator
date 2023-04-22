package com.translator.model.datasource

import com.translator.application.App
import com.translator.model.data.DataModel
import io.reactivex.rxjava3.core.Observable

class DataSourceRemote(private val remoteProvider: RetrofitImplementation =
    RetrofitImplementation().apply {
        App.instance.appComponent.inject(this) }) :
    DataSource<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> = remoteProvider.getData(word)
}
