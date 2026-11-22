package com.example.storemotos

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.storemotos.databinding.BiometricBinding

class Biometric : AppCompatActivity() {

    private lateinit var binding: BiometricBinding
    private var autenticado = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BiometricBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBloqueoAtras()
        verificarYMostrarBiometria()

        binding.btnHuella.setOnClickListener {
            verificarYMostrarBiometria()
        }
    }

    private fun configurarBloqueoAtras() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!autenticado) {
                    binding.tvEstadoBiometrico.text = "Debes autenticarte con huella para continuar."
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun verificarYMostrarBiometria() {
        val biometricManager = BiometricManager.from(this)
        val authenticators = BIOMETRIC_STRONG or DEVICE_CREDENTIAL

        when (biometricManager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                lanzarPromptBiometrico()
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                binding.tvEstadoBiometrico.text = "no se puede por qué no tiene sensor de huella"
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                binding.tvEstadoBiometrico.text = "El sensor biométrico no está disponible en este momento."
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                binding.tvEstadoBiometrico.text = "No hay huella dactilar registrada. Configura una en Ajustes del teléfono."
            }
            else -> {
                binding.tvEstadoBiometrico.text = "no se puede por qué no tiene sensor de huella"
            }
        }
    }

    private fun lanzarPromptBiometrico() {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                autenticado = true
                binding.tvEstadoBiometrico.text = "Autenticación exitosa. Entrando a StoreMotos..."

                val intent = Intent(this@Biometric, Menu::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                binding.tvEstadoBiometrico.text = "Huella no reconocida. Inténtalo de nuevo."
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                when (errorCode) {
                    BiometricPrompt.ERROR_USER_CANCELED,
                    BiometricPrompt.ERROR_NEGATIVE_BUTTON,
                    BiometricPrompt.ERROR_CANCELED -> {
                        binding.tvEstadoBiometrico.text = "Autenticación cancelada por el usuario."
                    }
                    BiometricPrompt.ERROR_LOCKOUT -> {
                        binding.tvEstadoBiometrico.text = "Demasiados intentos fallidos. Espera unos segundos e intenta de nuevo."
                    }
                    BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> {
                        binding.tvEstadoBiometrico.text = "Sensor bloqueado temporalmente por seguridad. Usa el bloqueo del dispositivo."
                    }
                    BiometricPrompt.ERROR_NO_BIOMETRICS,
                    BiometricPrompt.ERROR_HW_NOT_PRESENT -> {
                        binding.tvEstadoBiometrico.text = "No se puede acceder por qué no tiene sensor de huella"
                    }
                    else -> {
                        binding.tvEstadoBiometrico.text = "Error de autenticación: $errString"
                    }
                }
            }
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("StoreMotos - Inicio de sesión")
            .setSubtitle("Verifica tu identidad para acceder al inventario")
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()

        val biometricPrompt = BiometricPrompt(this, executor, callback)
        biometricPrompt.authenticate(promptInfo)
    }
}