package com.example.juegopalabras

import retrofit2.http.GET
import retrofit2.http.Path

// Define la interfaz de Retrofit para la API del diccionario
interface DictionaryApiService {
    @GET("api/v2/entries/en/{word}")
    suspend fun getDefinition(@Path("word") word: String): DefinitionResponse
}

