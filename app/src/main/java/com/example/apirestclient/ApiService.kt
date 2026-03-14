package com.example.apirestclient

import com.example.apirestclient.model.AuthResponse
import com.example.apirestclient.model.UserRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    // Ejercicio 1: Verifica que la API está activa
    @GET("/")
    fun verificarEstado(): Call<String>

    // Ejercicio 2: Registra un nuevo usuario
    @POST("/register")
    fun registrarUsuario(@Body request: UserRequest): Call<AuthResponse>

    // Ejercicio 3: Iniciar sesión de un usuario existente
    @POST("/login")
    fun iniciarSesion(@Body request: UserRequest): Call<AuthResponse>
}