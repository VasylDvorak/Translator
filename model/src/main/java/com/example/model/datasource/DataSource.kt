package com.example.model.datasource

import com.example.model.data.DataModel


interface DataSource<T> {

   suspend fun getData(word: String): List<DataModel>

}
