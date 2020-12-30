package org.viper75.image_search_kotlin.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import org.viper75.image_search_kotlin.api.UnsplashApi
import org.viper75.image_search_kotlin.models.UnsplashPagingSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashPhotoRepository @Inject constructor(private val unsplashApi: UnsplashApi) {

    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UnsplashPagingSource(unsplashApi, query) }
        ).liveData
}