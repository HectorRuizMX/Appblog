package com.example.appblog.domain.home

import com.example.appblog.core.Result
import com.example.appblog.data.model.Post
import com.example.appblog.data.remote.home.HomeScreenDataSource

class HomeScreenRepoImpl(private val dataSource: HomeScreenDataSource): HomeScreenRepo {

    override suspend fun getLatestPosts(): Result<List<Post>> = dataSource.getLatestPosts()
}