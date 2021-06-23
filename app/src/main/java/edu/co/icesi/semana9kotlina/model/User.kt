package edu.co.icesi.semana9kotlina.model

data class User(
    val id: String,
    val nombre: String,
    val date: Long
){
    override fun toString(): String {
        return this.nombre
    }
}
