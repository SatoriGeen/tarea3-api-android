package com.example.apirestclient

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val tvUsernameWelcome = findViewById<TextView>(R.id.tvUsernameWelcome)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // Obtener el nombre de usuario pasado desde LoginActivity
        val username = intent.getStringExtra("USERNAME")
        tvUsernameWelcome.text = "Hola, $username"

        btnLogout.setOnClickListener {
            // Regresar al login o cerrar sesión
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}