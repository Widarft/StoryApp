package com.dicoding.storyapp.data

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.storyapp.data.local.StoryDatabase
import com.dicoding.storyapp.data.local.StoryEntity
import com.dicoding.storyapp.data.retrofit.ApiService
import com.dicoding.storyapp.data.response.ListStoryItem
import com.dicoding.storyapp.data.response.StoryResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {

    private val mockApi = Mockito.mock(ApiService::class.java)
    private val mockDb = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val token = "your_token"
        val remoteMediator = StoryRemoteMediator(mockApi, mockDb, token)

        val pagingState = PagingState<Int, StoryEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val storyList = List(20) { index ->
            ListStoryItem(
                id = "id_$index",
                name = "name_$index",
                description = "description_$index",
                photoUrl = "photoUrl_$index",
                lon = index.toDouble(),
                lat = index.toDouble()
            )
        }

        val response = StoryResponse(listStory = storyList)

        whenever(mockApi.getStories("Bearer $token", 1, 10)).thenReturn(response)

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.close()
    }
}