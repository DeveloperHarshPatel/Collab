package com.developer_harshpatel.demo.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Entity represent table
// table for maintain history
@Entity(tableName = "historyTable")
class History (
    @ColumnInfo(name = "userId")val userId :Int,
    @ColumnInfo(name = "userName")val userName :String,
    @ColumnInfo(name = "filePath")val filePath :String,
    @ColumnInfo(name = "isAudio")val isAudio :Boolean,
    @ColumnInfo(name = "timestamp")val timeStamp :String
    ) {
    @PrimaryKey(autoGenerate = true) var id = 0
}