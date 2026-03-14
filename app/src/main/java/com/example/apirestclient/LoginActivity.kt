package com.example.apirestclient

import android.content.Intent
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

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.etUsernameLogin)
        val etPassword = findViewById<EditText>(R.id.etPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvLoginResult = findViewById<TextView>(R.id.tvLoginResult)

        // CAMBIO: Se usa 10.0.2.2 para conectar desde el emulador al host (PC)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                tvLoginResult.text = "Por favor llena todos los campos"
                return@setOnClickListener
            }

            val request = UserRequest(username, password)

            api.iniciarSesion(request).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    if (response.isSuccessful) {
                        // Login exitoso, navegamos a WelcomeActivity
                        val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
                        intent.putExtra("USERNAME", username)
                        startActivity(intent)
                        finish()
                    } else {
                        tvLoginResult.setTextColor(android.graphics.Color.RED)
                        tvLoginResult.text = "Error: Credenciales incorrectas"
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    tvLoginResult.setTextColor(android.graphics.Color.RED)
                    tvLoginResult.text = "Error de conexión:\n${t.message}"
                }
            })
        }
    }
}