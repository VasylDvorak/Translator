package com.translator.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.translator.room.favorite.FavoriteDao
import com.translator.room.favorite.FavoriteEntity
import com.translator.room.history.HistoryDao
import com.translator.room.history.HistoryEntity

@Database(
    entities = arrayOf(HistoryEntity::class, FavoriteEntity::class),
    version = 1,
    exportSchema = false
)
abstract class HistoryFavoriteDataBase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao
    abstract fun favoriteDao(): FavoriteDao
}
