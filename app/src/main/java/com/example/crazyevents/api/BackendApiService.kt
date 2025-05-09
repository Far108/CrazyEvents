package com.example.crazyevents.api

import com.example.crazyevents.data.Event
import retrofit2.Response
import retrofit2.http.GET

interface BackendApiService {
    @GET("/events") // Replace with the actual endpoint that returns the list of events
    suspend fun getEvents(): Response<List<Event>>
}