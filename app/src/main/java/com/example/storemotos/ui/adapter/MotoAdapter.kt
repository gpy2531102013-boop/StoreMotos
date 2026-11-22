package com.example.storemotos.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.storemotos.data.Moto
import com.example.storemotos.databinding.ItemMotoBinding

class MotoAdapter(
    private var listaMotos: List<Moto>,
    private val onEditarClick: (Moto) -> Unit,
    private val onEliminarClick: (Moto) -> Unit
) : RecyclerView.Adapter<MotoAdapter.MotoViewHolder>() {

    inner class MotoViewHolder(val binding: ItemMotoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MotoViewHolder {
        val binding = ItemMotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MotoViewHolder, position: Int) {
        val moto = listaMotos[position]
        with(holder.binding) {
            tvMarcaModelo.text = "${moto.marca} ${moto.modelo}"
            tvCilindraje.text = "Cilindraje: ${moto.cilindraje} cc"
            tvPrecioStock.text = "Precio: $${moto.precio} | Stock: ${moto.stock}"

            btnEditarMoto.setOnClickListener { onEditarClick(moto) }
            btnEliminarMoto.setOnClickListener { onEliminarClick(moto) }
        }
    }

    override fun getItemCount(): Int = listaMotos.size

    fun actualizarLista(nuevaLista: List<Moto>) {
        listaMotos = nuevaLista
        notifyDataSetChanged()
    }
}