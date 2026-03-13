package com.example.apirestclient

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    // Apunta a la ruta raíz que pide el profesor
    @GET("/")
    fun verificarEstado(): Call<String>
}