package com.example.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.room.favorite.FavoriteDao
import com.example.room.favorite.FavoriteEntity
import com.example.room.history.HistoryDao
import com.example.room.history.HistoryEntity


@Database(
    entities = arrayOf(HistoryEntity::class, FavoriteEntity::class),
    version = 1,
    exportSchema = false
)
abstract class HistoryFavoriteDataBase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao
    abstract fun favoriteDao(): FavoriteDao
}
