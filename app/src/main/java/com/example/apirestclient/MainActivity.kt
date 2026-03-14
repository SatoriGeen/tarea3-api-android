package com.example.apirestclient

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

        val tvApiResponse = findViewById<TextView>(R.id.tvApiResponse)
        val btnGoToRegister = findViewById<Button>(R.id.btnGoToRegister)
        val btnGoToLogin = findViewById<Button>(R.id.btnGoToLogin)

        btnGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnGoToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // CAMBIO: Se usa 10.0.2.2 para conectar desde el emulador al host (PC)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.66:5000/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.verificarEstado().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    tvApiResponse.text = "Servidor Activo:\n${response.body()}"
                } else {
                    tvApiResponse.text = "Error de conexión: Código ${response.code()}"
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                tvApiResponse.text = "Error Crítico: No se pudo conectar a la API.\nVerifica que Docker esté corriendo y usa la IP correcta."
            }
        })
    }
}