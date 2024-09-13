package com.example.juegopalabras

data class DefinitionResponse(
    val word: String,
    val meanings: List<Meaning>
)

data class Meaning(
    val definitions: List<Definition>
)

data class Definition(
    val definition: String
)

