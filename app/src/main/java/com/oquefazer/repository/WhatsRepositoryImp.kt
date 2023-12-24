package com.example.oquefazer.repository

import androidx.lifecycle.LiveData
import com.oquefazer.data.local.WhatsDaoSource
import com.oquefazer.data.local.WhatsToDo
import com.oquefazer.repository.Repository


class WhatsRepositoryImp(private val dao : WhatsDaoSource) : Repository {

    override fun showAllTasks(): LiveData<List<WhatsToDo>> {
        return dao.getAllTask()
    }

    override suspend fun insertSingleTask(task: WhatsToDo) {
        dao.insertTask(task)
    }

    override suspend fun deleteSingleTask(task: WhatsToDo) {
        dao.deleteTask(task)
    }

    override suspend fun updateSingleTask(task: WhatsToDo) {
        dao.updateTask(task)
    }

}