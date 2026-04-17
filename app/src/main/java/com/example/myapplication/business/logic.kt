package com.example.myapplication.business

import com.example.myapplication.data.Asistente
import com.example.myapplication.data.RegistroState

fun Int.esMayorDeEdad(): Boolean = this >= 18

fun String.esNombreValido(): Boolean = this.isNotBlank()

fun procesarAsistente(
    asistente: Asistente,
    validacionExtra: (Asistente) -> Boolean
): Boolean {
    return validacionExtra(asistente)
}

fun registrarAsistente(
    nombreInput: String?,
    edadInput: String?,
    tipoEntradaInput: String?
): RegistroState {

    val edad = edadInput?.toIntOrNull()

    return nombreInput?.let { nombre ->
        tipoEntradaInput?.let { tipo ->

            val asistente = Asistente(
                nombre = nombre,
                edad = edad,
                tipoEntrada = tipo
            )

            if (!nombre.esNombreValido()) {
                RegistroState.Error("Nombre inválido")
            } else if (edad == null) {
                RegistroState.Error("Edad inválida o nula")
            } else if (!edad.esMayorDeEdad()) {
                RegistroState.Error("El asistente es menor de edad")
            } else {
                val valido = procesarAsistente(asistente) { a ->
                    a.tipoEntrada != "Prohibida"
                }

                if (valido) {
                    RegistroState.Success(
                        asistente,
                        "Registro exitoso: ${asistente.nombre}, ${asistente.edad} años"
                    )
                } else {
                    RegistroState.Error("Tipo de entrada no permitido")
                }
            }

        }
    } ?: RegistroState.Error("Datos incompletos")
}