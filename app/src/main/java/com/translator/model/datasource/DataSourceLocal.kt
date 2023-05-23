package com.translator.model.datasource

import com.translator.model.data.DataModel
import io.reactivex.rxjava3.core.Observable


class DataSourceLocal(private val remoteProvider: RoomDataBaseImplementation =
    RoomDataBaseImplementation()) : DataSource<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> = remoteProvider.getData(word)
}
