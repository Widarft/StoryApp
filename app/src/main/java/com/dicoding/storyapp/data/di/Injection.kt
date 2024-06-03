package com.dicoding.storyapp.data.di

import android.content.Context
import com.dicoding.storyapp.data.local.StoryDatabase
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.pref.dataStore
import com.dicoding.storyapp.data.retrofit.ApiConfig

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val storyDatabase = StoryDatabase.getDatabase(context)
        return UserRepository.getInstance(userPreference, apiService, storyDatabase)
    }


    fun provideUploadRepository(): UploadRepository {
        return UploadRepository.getInstance(ApiConfig.getApiService())
    }

    fun provideDetailRepository(userPreference: UserPreference): DetailRepository {
        return DetailRepository.getInstance(ApiConfig.getApiService(), userPreference)
    }
}
