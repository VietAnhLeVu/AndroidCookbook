package com.example.androidcookbook.data.network

import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.user.User
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("profile/{userId}")
    suspend fun getUserProfile(@Path("userId") userId: Int): ApiResponse<User>

    @GET("profile/posts/{userId}")
    suspend fun getUserPosts(@Path("userId") userId: Int): ApiResponse<List<Post>>
}
