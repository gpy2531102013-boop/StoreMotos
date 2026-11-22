package com.example.storemotos

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storemotos.data.Moto
import com.example.storemotos.databinding.CatalogoBinding
import com.example.storemotos.ui.adapter.MotoAdapter
import com.google.firebase.firestore.FirebaseFirestore

class CatalogoMotos : AppCompatActivity() {

    private lateinit var binding: CatalogoBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: MotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        adapter = MotoAdapter(
            listaMotos = emptyList(),
            onEditarClick = { moto ->
                val intent = Intent(this, Registro::class.java).apply {
                    putExtra("motoId", moto.id)
                }
                startActivity(intent)
            },
            onEliminarClick = { moto ->
                eliminarMoto(moto)
            }
        )

        binding.rvMotos.layoutManager = LinearLayoutManager(this)
        binding.rvMotos.adapter = adapter

        binding.btnNuevaMotoCatalogo.setOnClickListener {
            startActivity(Intent(this, Registro::class.java))
        }

        binding.btnVolverMenuCatalogo.setOnClickListener {
            finish()
        }

        escucharMotosFirestore()
    }

    private fun escucharMotosFirestore() {
        db.collection("motos")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    binding.tvCatalogoEstado.text = "Error al cargar catálogo: ${error.message}"
                    return@addSnapshotListener
                }

                val motos = snapshots?.documents?.map { doc ->
                    Moto(
                        id = doc.id,
                        marca = doc.getString("marca") ?: "",
                        modelo = doc.getString("modelo") ?: "",
                        cilindraje = doc.getLong("cilindraje")?.toInt() ?: 0,
                        precio = doc.getDouble("precio") ?: 0.0,
                        stock = doc.getLong("stock")?.toInt() ?: 0
                    )
                } ?: emptyList()

                adapter.actualizarLista(motos)
                binding.tvCatalogoEstado.text = if (motos.isEmpty()) {
                    "No hay motos registradas todavía."
                } else {
                    "Total de modelos: ${motos.size}"
                }
            }
    }

    private fun eliminarMoto(moto: Moto) {
        if (moto.id.isEmpty()) return
        db.collection("motos").document(moto.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Moto eliminada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al eliminar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}