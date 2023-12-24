package com.oquefazer.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "O_que_fazer")
data class WhatsToDo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "title")
    var title : String,

    @ColumnInfo(name = "message")
    var message : String,

    @ColumnInfo(name = "hour")
    var hour : Long? = null,

) : Serializable