package com.translator.model.datasource


import com.translator.model.data.DataModel
import io.reactivex.rxjava3.core.Observable
import org.koin.java.KoinJavaComponent.getKoin


class RetrofitImplementation() : DataSource<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> {
        val getService= getKoin().get<ApiService>()
        return getService.search(word)
    }

}
