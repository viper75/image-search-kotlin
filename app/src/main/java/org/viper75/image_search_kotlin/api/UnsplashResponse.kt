package org.viper75.image_search_kotlin.api

import org.viper75.image_search_kotlin.models.UnsplashPhoto

data class UnsplashResponse(
    val results: List<UnsplashPhoto>
)