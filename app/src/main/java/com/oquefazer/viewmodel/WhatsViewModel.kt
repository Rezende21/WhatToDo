package com.example.oquefazer.ui.fragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oquefazer.data.local.WhatsToDo
import com.oquefazer.other.Resorce
import com.oquefazer.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WhatsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _taskStatus = MutableLiveData<Resorce<WhatsToDo>>()
    val taskStatus : LiveData<Resorce<WhatsToDo>> = _taskStatus

    private val _taskEdit = MutableLiveData<Resorce<WhatsToDo>>()
    val taskEdit : LiveData<Resorce<WhatsToDo>> = _taskEdit
    private var tasks : LiveData<List<WhatsToDo>> = MutableLiveData()

    private var _NotificationStatus = MutableLiveData<Boolean>()
    val NotificationStatus : LiveData<Boolean> = _NotificationStatus


    fun showAllTasks() : LiveData<List<WhatsToDo>> {
        viewModelScope.launch {
            tasks = repository.showAllTasks()
        }
        return tasks
    }

    fun deleteTask(task: WhatsToDo) {
        viewModelScope.launch {
            repository.deleteSingleTask(task)
        }
    }

    private fun insertTask(task: WhatsToDo) {
        viewModelScope.launch {
            repository.insertSingleTask(task)
        }
    }

    fun verifyTaskValue(taskName : String, message : String, hour : Long) {
        _taskStatus.postValue(Resorce.loading(null))
        if(taskName.isEmpty() && message.isEmpty()) {
            return _taskStatus.postValue(Resorce.error("Coloque um titulo ou uma menssagem", null))
        }

        if(hour <= 3) {
            _NotificationStatus.postValue(false)
        } else {
            _NotificationStatus.postValue(true)
        }
//TODO erro nesse codigo que foi criado
        val task = WhatsToDo(null, taskName, message, hour)
        insertTask(task)
        _taskStatus.postValue(Resorce.success(task))
    }

    fun checkTaskValue(id : Int, taskNome : String, message : String, hour : Long) {
        _taskEdit.postValue(Resorce.loading(null))
        if (taskNome.isBlank()) {
            return _taskEdit.postValue(Resorce.error("Não pode deixar esse campo vazio", null))
        }
        val task = WhatsToDo(id, taskNome, message, hour)
        updateTask(task)
    }

    private fun updateTask(task: WhatsToDo) {
        viewModelScope.launch {
            repository.updateSingleTask(task)
        }
        _taskEdit.postValue(Resorce.success(task))
    }

}