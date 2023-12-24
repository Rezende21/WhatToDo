package com.oquefazer.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.oquefazer.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
@ExperimentalCoroutinesApi
class WhatsDaoSourceTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var taskDatabase : WhatsDatabase
    private lateinit var dao : WhatsDaoSource

    @Before
    fun setup() {
        taskDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WhatsDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao = taskDatabase.getInstance()
    }

    @After
    fun teardown() {
        taskDatabase.close()
    }

    @Test
    fun insertTaskItem() = runBlocking {
        val task = WhatsToDo(1,"test", "16", "14/05")
        dao.insertTask(task)

        val showAllTasks = dao.getAllTask().getOrAwaitValue()
        assertThat(showAllTasks).contains(task)
    }

    fun deleteTaskItem() = runBlocking {
        val task = WhatsToDo(1, "test", "16", "14/5")
        dao.insertTask(task)
        dao.deleteTask(task)

        val showAllTask = dao.getAllTask().getOrAwaitValue()
        assertThat(showAllTask).doesNotContain(task)
    }

}