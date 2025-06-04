package com.example.crazyevents.api

import com.example.crazyevents.data.Event
import com.example.crazyevents.data.Following
import com.example.crazyevents.data.Poster
import com.example.crazyevents.data.UserProfile
import com.example.crazyevents.data.UserUpdateRequest
import com.example.crazyevents.model.AuthRequest
import com.example.crazyevents.model.AuthResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface BackendApiService {
    @GET("/events") // Replace with the actual endpoint that returns the list of events
    suspend fun getEvents(): Response<List<Event>>

    @GET("/myevents/{id}") // Replace with the actual endpoint that returns the list of events
    suspend fun getMyEvents(@Path("id") id: String): Response<List<Event>>

    @GET("/posters/{id}") // Replace with the actual endpoint that returns the list of posters
    suspend fun getPosters(@Path("id") id: String): Response<List<Poster>>

    @GET("/posters/toggle/{fid}/{uid}") // Replace with the actual endpoint that returns a specific event by ID
    suspend fun toggleFollow(@Path("fid") fid: String, @Path("uid") uid: String): Response<Poster>

    @GET("/posters/follow/{fid}/{uid}") // Replace with the actual endpoint that returns a specific event by ID
    suspend fun isFollowing(@Path("fid") fid: String?, @Path("uid") uid: String): Response<Following>

    @GET("/events/{eventId}")
    suspend fun getEventbyId(@Path("eventId") eventId: String): Response<Event>

    @POST("/event")
    suspend fun createEvent(@Body event: Event): Response<Event>

    @POST("/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("/register")
    suspend fun register(@Body request: AuthRequest): Response<Unit>

    @POST("/events/{eventId}/interest")
    suspend fun toggleEventInterest(
        @Path("eventId") eventId: String,
        @Header("Authorization") token: String
    ): Response<InterestResponse>

    @PUT("users/{id}")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Path("id") userId: String,
        @Body updateRequest: UserUpdateRequest
    ): Response<UserProfile>

    @Multipart
    @PUT("events/{id}/images")
    suspend fun uploadEventImages(
        @Header("Authorization") token: String,
        @Path("id") eventId: String,
        @Part images: List<MultipartBody.Part>
    ): Response<Event>

    @GET("events/{id}")
    suspend fun getEventById(@Path("id") id: String): Response<Event>

}

data class InterestResponse(
    val message: String,
    val goingTo: List<String>
)
