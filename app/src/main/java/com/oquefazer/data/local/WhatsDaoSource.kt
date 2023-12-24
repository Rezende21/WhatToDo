package com.oquefazer.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WhatsDaoSource {

    @Query("SELECT * FROM O_que_fazer")
    fun getAllTask() : LiveData<List<WhatsToDo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task : WhatsToDo)

    @Delete
    suspend fun deleteTask(task : WhatsToDo)

    @Update
    suspend fun updateTask(task: WhatsToDo)

}