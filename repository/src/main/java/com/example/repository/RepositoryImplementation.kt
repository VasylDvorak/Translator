package com.example.repository

import com.example.model.data.DataModel
import com.example.model.datasource.DataSource


class RepositoryImplementation(private val dataSource: DataSource<List<DataModel>>) :
    Repository<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }

    override suspend fun getFavoriteList(): List<DataModel> = listOf()

}
