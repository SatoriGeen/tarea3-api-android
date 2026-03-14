package com.example.apirestclient

import android.content.Intent // <-- Importación necesaria para navegar
import android.os.Bundle
import android.widget.Button // <-- Importación necesaria para el botón
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enlazamos las vistas del XML con nuestro código
        val tvApiResponse = findViewById<TextView>(R.id.tvApiResponse)
        val btnGoToRegister = findViewById<Button>(R.id.btnGoToRegister) // <-- Buscamos tu nuevo botón

        // --- LÓGICA DE NAVEGACIÓN ---
        btnGoToRegister.setOnClickListener {
            // Creamos el "boleto de viaje" desde esta Activity hacia RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent) // ¡Arranca el viaje a la otra pantalla!
        }

        // --- LÓGICA DE RED (EJERCICIO 1) ---
        // 1. Configurar Retrofit con la IP del dispositivo físico
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.66:5000/")
            .addConverterFactory(ScalarsConverterFactory.create()) // Para recibir texto simple
            .build()

        // 2. Crear el servicio
        val api = retrofit.create(ApiService::class.java)

        // 3. Ejecutar la petición GET de forma asíncrona (en segundo plano)
        api.verificarEstado().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    // Si el servidor responde bien (código 200), mostramos el mensaje
                    tvApiResponse.text = "Respuesta del servidor:\n${response.body()}"
                } else {
                    tvApiResponse.text = "Error de conexión: Código ${response.code()}"
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                // Si el servidor está apagado o no hay internet
                tvApiResponse.text = "Error crítico:\n${t.message}"
            }
        })
    }
}