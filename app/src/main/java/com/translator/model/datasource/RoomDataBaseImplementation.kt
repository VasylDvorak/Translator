package com.translator.model.datasource

import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.room.favorite.FavoriteDao
import com.translator.room.history.HistoryDao
import com.translator.utils.convertDataModelSuccessToEntity
import com.translator.utils.converterDataModelToFavoriteEntity
import com.translator.utils.mapFavoriteEntityToSearchResult
import com.translator.utils.mapHistoryEntityToSearchResult

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
