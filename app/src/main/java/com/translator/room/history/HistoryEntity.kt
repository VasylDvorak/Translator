package com.translator.room.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.translator.model.data.Translation

@Entity(tableName ="HistoryEntityTable",indices = arrayOf(Index(value = arrayOf("word"), unique = true)))
data class HistoryEntity(
    @field:PrimaryKey
    @field:ColumnInfo(name = "word") var word: String,
    @field:ColumnInfo(name = "imageUrl") var imageUrl: String?,
    @field:ColumnInfo(name = "transcription")  var transcription: String?,
    @field:ColumnInfo(name = "soundUrl")  var  soundUrl:String?,
    @field:ColumnInfo(name = "translation") var translation: String?
)

