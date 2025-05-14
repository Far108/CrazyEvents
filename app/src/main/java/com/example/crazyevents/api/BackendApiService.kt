package com.example.crazyevents.api

import com.example.crazyevents.data.Event
import com.example.crazyevents.data.Poster
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BackendApiService {
    @GET("/events") // Replace with the actual endpoint that returns the list of events
    suspend fun getEvents(): Response<List<Event>>

    @GET("/posters") // Replace with the actual endpoint that returns the list of posters
    suspend fun getPosters(): Response<List<Poster>>

    @GET("/posters/toggle/{id}") // Replace with the actual endpoint that returns a specific event by ID
    suspend fun toggleFollow(@Path("id") userId: String): Response<Poster>

    @GET("/events/{eventId}")
    suspend fun getEventbyId(@Path("eventId") eventId: String): Response<Event>
}