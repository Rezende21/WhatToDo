package com.oquefazer.di

import android.content.Context
import androidx.room.Room
import com.example.oquefazer.repository.WhatsRepositoryImp
import com.oquefazer.data.local.WhatsDaoSource
import com.oquefazer.data.local.WhatsDatabase
import com.oquefazer.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideInstanceFromDB(@ApplicationContext context : Context) : WhatsDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            WhatsDatabase::class.java,
            "WHATS_TO_DO.db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideTaskDao(database: WhatsDatabase) : WhatsDaoSource = database.getInstance()

    @Provides
    @Singleton
    fun provideRepositoryTask(dao : WhatsDaoSource) : Repository {
        return WhatsRepositoryImp(dao)
    }
}