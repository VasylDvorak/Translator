package com.translator.model.repository

import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.model.datasource.DataSourceLocal


class RepositoryImplementationLocal(private val dataSource: DataSourceLocal<List<DataModel>>) :
    RepositoryLocal<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }

    override suspend fun getFavoriteList(): List<DataModel> {
        return dataSource.getFavoriteList()
    }

    override suspend fun saveToDB(appState: AppState) {
        dataSource.saveToDB(appState)
    }

    override suspend fun findWordInDB(word: String) : DataModel{
      return  dataSource.findWordFromDB(word)
    }

    override suspend fun putFavorite(favorite: DataModel) {
        dataSource.putFavorite(favorite)
    }

    override suspend fun removeFavoriteItem(favorite: DataModel) {
        dataSource.removeFavoriteItem(favorite)
    }

}
