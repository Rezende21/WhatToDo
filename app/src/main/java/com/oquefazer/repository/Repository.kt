package com.oquefazer.repository

import androidx.lifecycle.LiveData
import com.oquefazer.data.local.WhatsToDo

interface Repository {

    fun showAllTasks() : LiveData<List<WhatsToDo>>
    suspend fun insertSingleTask(task : WhatsToDo)
    suspend fun deleteSingleTask(task : WhatsToDo)
    suspend fun updateSingleTask(task : WhatsToDo)
}