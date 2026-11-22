package com.example.storemotos

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.storemotos.databinding.SensorBinding
import kotlin.math.atan2
import kotlin.math.sqrt

class Sensor : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: SensorBinding
    private lateinit var sensorManager: SensorManager
    private var acelerometro: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SensorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (acelerometro == null) {
            binding.tvSensorMensaje.text = "Este dispositivo no cuenta con sensor acelerómetro."
            binding.tvEstadoEstabilidad.text = "Estado: Sensor no disponible"
        } else {
            binding.tvSensorMensaje.text = "Mueve o inclina el dispositivo para ver las lecturas."
        }

        binding.btnVolverMenuSensor.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        acelerometro?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val inclinacionRad = atan2(x.toDouble(), sqrt((y * y + z * z).toDouble()))
        val inclinacionGrados = Math.toDegrees(inclinacionRad)
        val anguloAbsoluto = kotlin.math.abs(inclinacionGrados)

        binding.tvEjeX.text = "Eje X: ${String.format("%.2f", x)}"
        binding.tvEjeY.text = "Eje Y: ${String.format("%.2f", y)}"
        binding.tvEjeZ.text = "Eje Z: ${String.format("%.2f", z)}"
        binding.tvAnguloInclinacion.text = "${String.format("%.1f", inclinacionGrados)}°"

        binding.tvEstadoEstabilidad.text = when {
            anguloAbsoluto < 10.0 -> "Estado: Posición nivelada"
            anguloAbsoluto < 30.0 -> "Estado: Inclinación moderada"
            else -> "Estado: Inclinación pronunciada"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Sin cambios obligatorios en UI
    }
}