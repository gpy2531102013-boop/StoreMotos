package com.example.storemotos

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.storemotos.databinding.RegistroBinding
import com.google.firebase.firestore.FirebaseFirestore

class Registro : AppCompatActivity() {

    private lateinit var binding: RegistroBinding
    private lateinit var db: FirebaseFirestore
    private var motoIdEditar: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        motoIdEditar = intent.getStringExtra("motoId")
        if (motoIdEditar != null) {
            binding.tvRegistroTitulo.text = "Editar Moto"
            binding.btnGuardarMoto.text = "Actualizar moto"
            cargarMotoExistente(motoIdEditar!!)
        }

        binding.btnGuardarMoto.setOnClickListener {
            guardarMoto()
        }

        binding.btnVolverMenu.setOnClickListener {
            finish()
        }
    }

    private fun cargarMotoExistente(id: String) {
        binding.tvEstadoRegistro.text = "Cargando datos..."
        db.collection("motos").document(id)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    binding.etMarca.setText(doc.getString("marca") ?: "")
                    binding.etModelo.setText(doc.getString("modelo") ?: "")
                    val cc = doc.getLong("cilindraje")?.toInt() ?: 0
                    val precio = doc.getDouble("precio") ?: 0.0
                    val stock = doc.getLong("stock")?.toInt() ?: 0
                    binding.etCilindraje.setText(if (cc > 0) cc.toString() else "")
                    binding.etPrecio.setText(if (precio > 0.0) precio.toString() else "")
                    binding.etStock.setText(if (stock > 0) stock.toString() else "")
                    binding.tvEstadoRegistro.text = ""
                } else {
                    binding.tvEstadoRegistro.text = "No se encontró el registro."
                }
            }
            .addOnFailureListener { e ->
                binding.tvEstadoRegistro.text = "Error al cargar: ${e.message}"
            }
    }

    private fun guardarMoto() {
        val marca = binding.etMarca.text.toString().trim()
        val modelo = binding.etModelo.text.toString().trim()
        val cilindrajeTexto = binding.etCilindraje.text.toString().trim()
        val precioTexto = binding.etPrecio.text.toString().trim()
        val stockTexto = binding.etStock.text.toString().trim()

        if (marca.isEmpty()) {
            binding.etMarca.error = "Ingresa la marca"
            binding.etMarca.requestFocus()
            return
        }

        if (modelo.isEmpty()) {
            binding.etModelo.error = "Ingresa el modelo"
            binding.etModelo.requestFocus()
            return
        }

        val cilindraje = cilindrajeTexto.toIntOrNull()
        if (cilindraje == null || cilindraje <= 0) {
            binding.etCilindraje.error = "Ingresa un cilindraje válido"
            binding.etCilindraje.requestFocus()
            return
        }

        val precio = precioTexto.toDoubleOrNull()
        if (precio == null || precio <= 0.0) {
            binding.etPrecio.error = "Ingresa un precio válido"
            binding.etPrecio.requestFocus()
            return
        }

        val stock = stockTexto.toIntOrNull()
        if (stock == null || stock < 0) {
            binding.etStock.error = "Ingresa un stock válido (0 o mayor)"
            binding.etStock.requestFocus()
            return
        }

        binding.btnGuardarMoto.isEnabled = false
        binding.tvEstadoRegistro.text = "Guardando en Firestore..."

        val motoData = hashMapOf(
            "marca" to marca,
            "modelo" to modelo,
            "cilindraje" to cilindraje,
            "precio" to precio,
            "stock" to stock
        )

        val coleccion = db.collection("motos")
        val tarea = if (motoIdEditar != null) {
            coleccion.document(motoIdEditar!!).set(motoData)
        } else {
            coleccion.add(motoData)
        }

        tarea
            .addOnSuccessListener {
                binding.btnGuardarMoto.isEnabled = true
                val mensaje = if (motoIdEditar != null) "Moto actualizada" else "Moto registrada"
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                binding.btnGuardarMoto.isEnabled = true
                binding.tvEstadoRegistro.text = "Error al guardar: ${e.message}"
            }
    }
}