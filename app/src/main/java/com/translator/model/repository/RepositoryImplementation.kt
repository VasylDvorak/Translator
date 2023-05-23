package com.translator.model.repository

import com.translator.model.data.DataModel
import com.translator.model.datasource.DataSource
import io.reactivex.rxjava3.core.Observable


class RepositoryImplementation(private val dataSource: DataSource<List<DataModel>>) :
    Repository<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> {
        return dataSource.getData(word)
    }
}
