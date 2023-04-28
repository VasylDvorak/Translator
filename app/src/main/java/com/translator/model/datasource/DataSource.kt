package com.translator.model.datasource

import com.translator.model.data.DataModel


interface DataSource<T : Any> {

   suspend fun getData(word: String): List<DataModel>
}
