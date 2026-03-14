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

        // CAMBIO: Se usa 10.0.2.2 para conectar desde el emulador al host (PC)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.66:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                tvRegisterResult.text = "Por favor llena todos los campos"
                return@setOnClickListener
            }

            val request = UserRequest(username, password)

            api.registrarUsuario(request).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    if (response.isSuccessful) {
                        tvRegisterResult.setTextColor(android.graphics.Color.GREEN)
                        tvRegisterResult.text = "Éxito: ${response.body()?.message}"
                    } else {
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