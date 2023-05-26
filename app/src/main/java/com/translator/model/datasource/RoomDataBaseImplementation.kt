package com.translator.model.datasource


import com.example.model.data.AppState
import com.example.model.data.DataModel
import com.example.model.datasource.DataSourceLocal
import com.example.room.favorite.FavoriteDao
import com.example.room.history.HistoryDao
import com.example.utils.convertDataModelSuccessToEntity
import com.example.utils.converterDataModelToFavoriteEntity
import com.example.utils.mapFavoriteEntityToSearchResult
import com.example.utils.mapHistoryEntityToSearchResult

class RoomDataBaseImplementation(
    private val historyDao: HistoryDao,
    private val favoriteDao: FavoriteDao
) :
    DataSourceLocal<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return mapHistoryEntityToSearchResult(historyDao.all())
    }

    override suspend fun saveToDB(appState: AppState) {
        convertDataModelSuccessToEntity(appState)?.let {
            historyDao.insert(it)
        }
    }

    override suspend fun findWordFromDB(word: String): DataModel {
        return mapHistoryEntityToSearchResult(listOf(historyDao.getDataByWord(word))).get(0)
    }

    override suspend fun putFavorite(favorite: DataModel) {
        favoriteDao.insert(converterDataModelToFavoriteEntity(favorite))
    }

    override suspend fun getFavoriteList(): List<DataModel> {
        return mapFavoriteEntityToSearchResult(favoriteDao.all())
    }

    override suspend fun removeFavoriteItem(favorite: DataModel) {
        favorite.text?.let { favoriteDao.deleteItem(it) }
    }
}
