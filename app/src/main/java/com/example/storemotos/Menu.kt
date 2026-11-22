package com.example.storemotos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.storemotos.databinding.MenuBinding

class Menu : AppCompatActivity() {

    private lateinit var binding: MenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardCatalogo.setOnClickListener {
            startActivity(Intent(this, CatalogoMotos::class.java))
        }

        binding.cardRegistro.setOnClickListener {
            startActivity(Intent(this, Registro::class.java))
        }

        binding.cardSensor.setOnClickListener {
            startActivity(Intent(this, Sensor::class.java))
        }

        binding.btnCerrarSesion.setOnClickListener {
            val intent = Intent(this, Biometric::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}