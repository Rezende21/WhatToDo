package com.oquefazer.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.oquefazer.ui.fragment.viewmodel.WhatsViewModel
import com.google.common.truth.Truth.assertThat
import com.oquefazer.MaincoroutineRule
import com.oquefazer.getOrAwaitValueTest
import com.oquefazer.other.Status
import com.oquefazer.repository.FakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LisAndAddViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MaincoroutineRule()

    private lateinit var viewModel: WhatsViewModel

    @Before
    fun setup() {
        viewModel = WhatsViewModel(FakeRepository())
    }

    @Test
    fun `insert task item with empty field, return error`() {
        viewModel.verifyTaskValue("", "14:00", "14/5")
        val value = viewModel.taskStatus.getOrAwaitValueTest()

        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert task item with everything ok, return success`() {
        viewModel.verifyTaskValue("name","14:00","145")
        val value  = viewModel.taskStatus.getOrAwaitValueTest()

        assertThat(value.status).isEqualTo(Status.SUCCESS)
    }
}