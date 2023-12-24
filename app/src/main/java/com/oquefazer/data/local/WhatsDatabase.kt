package com.oquefazer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WhatsToDo::class], version = 3, exportSchema = false)
abstract class WhatsDatabase : RoomDatabase() {

    abstract fun getInstance() : WhatsDaoSource
}