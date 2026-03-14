package com.example.apirestclient

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.apirestclient.model.AuthResponse
import com.example.apirestclient.model.UserRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvRegisterResult = findViewById<TextView>(R.id.tvRegisterResult)

        // 1. Configurar Retrofit agregando GsonConverterFactory
        val retrofit = Retrofit.Builder()
            // OJO: Si sigues probando en tu celular físico, usa tu IP en lugar de 10.0.2.2
            .baseUrl("http://192.168.100.66:5000/")
            .addConverterFactory(GsonConverterFactory.create()) // <-- Vital para enviar/recibir JSON
            .build()

        val api = retrofit.create(ApiService::class.java)

        // 2. Acción al presionar el botón
        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                tvRegisterResult.text = "Por favor llena todos los campos"
                return@setOnClickListener
            }

            // 3. Empaquetar los datos usando el modelo que creaste
            val request = UserRequest(username, password)

            // 4. Hacer la petición POST
            api.registrarUsuario(request).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    if (response.isSuccessful) {
                        // Código 200: Registro exitoso
                        tvRegisterResult.setTextColor(android.graphics.Color.GREEN)
                        tvRegisterResult.text = "Éxito: ${response.body()?.message}"
                    } else {
                        // Código 400 u otro: Usuario duplicado o error
                        tvRegisterResult.setTextColor(android.graphics.Color.RED)
                        tvRegisterResult.text = "Error: El usuario ya existe o datos inválidos"
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    tvRegisterResult.setTextColor(android.graphics.Color.RED)
                    tvRegisterResult.text = "Error de conexión:\n${t.message}"
                }
            })
        }
    }
}