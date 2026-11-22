package com.example.storemotos.data

data class Moto(
    val id: String = "",
    val marca: String = "",
    val modelo: String = "",
    val cilindraje: Int = 0,
    val precio: Double = 0.0,
    val stock: Int = 0
)