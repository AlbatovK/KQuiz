package com.albatros.kquiz.model.api

import com.albatros.kquiz.model.data.ClientInfo
import com.albatros.kquiz.model.data.Quiz
import com.albatros.kquiz.model.data.ResponseData
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(value = "/quiz/get/all")
    suspend fun getQuizzes(): List<Quiz>

    @GET(value = "/session/register")
    suspend fun registerSession(@Query("quiz_id") quiz_id: Long): ResponseData

    @GET(value = "/session/enter")
    suspend fun enterSession(
        @Query("session_id") session_id: Long,
        @Query("name") name: String
    ): ResponseData

    @GET(value = "/session/info/get/all")
    suspend fun getClientsInfo(@Query("session_id") session_id: Long): List<ClientInfo>

    @GET(value = "session/info/started")
    suspend fun hasStarted(@Query("session_id") session_id: Long): Boolean

    @GET(value = "/session/quiz/get")
    suspend fun getCurrentQuiz(@Query("session_id") session_id: Long): Quiz

    @GET(value = "/session/start")
    suspend fun startSession(@Query("session_id") session_id: Long)
}