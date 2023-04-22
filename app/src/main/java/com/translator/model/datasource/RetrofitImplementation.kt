package com.translator.model.datasource

import com.translator.model.data.DataModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RetrofitImplementation : DataSource<List<DataModel>> {
    @Inject
    lateinit var getService: ApiService
    override fun getData(word: String): Observable<List<DataModel>> {
        return getService.search(word)
    }

}
