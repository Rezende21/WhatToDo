package com.oquefazer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oquefazer.data.local.WhatsToDo

class FakeRepository : Repository {

    private val tasks = mutableListOf<WhatsToDo>()
    private val showAllTask = MutableLiveData<List<WhatsToDo>>()

    private fun refreshLiveData() {
        showAllTask.postValue(tasks)
    }

    override fun showAllTasks(): LiveData<List<WhatsToDo>> {
        return showAllTask
    }

    override suspend fun insertSingleTask(task: WhatsToDo) {
        tasks.add(task)
        refreshLiveData()
    }

    override suspend fun deleteSingleTask(task: WhatsToDo) {
        tasks.remove(task)
        refreshLiveData()
    }

    override suspend fun updateSingleTask(task: WhatsToDo) {
        val taskId = task.id
        if (taskId != null) {
            tasks.removeAt(taskId)
        }
        tasks.add(task)
        refreshLiveData()
    }
}