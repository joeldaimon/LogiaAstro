package com.joguco.logiaastro.model

data class ChatMessage(
    var usuario: String,
    var texto: String){

    override fun toString(): String {
        return "${usuario.uppercase()}\n$texto"
    }
}