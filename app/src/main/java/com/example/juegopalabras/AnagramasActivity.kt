package com.example.juegopalabras

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AnagramActivity : AppCompatActivity() {

    private val wordList = listOf("animal", "casa", "perro", "gato", "colores", "sol", "estrella")
    private lateinit var scrambledWord: String
    private lateinit var correctWord: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anagram)

        val tvScrambled = findViewById<TextView>(R.id.tvScrambledWord)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        // Botón para comprobar la respuesta
        findViewById<Button>(R.id.btnCheckAnswer).setOnClickListener {
            val userInput = findViewById<TextView>(R.id.etUserInput).text.toString()
            if (userInput.equals(correctWord, ignoreCase = true)) {
                tvResult.text = "¡Correcto!"
                // Opción para consultar la definición
                findDefinition(correctWord)
            } else {
                tvResult.text = "¡Incorrecto! Intenta de nuevo."
            }
        }

        // Genera un anagrama aleatorio al iniciar
        generateAnagram(tvScrambled)
    }

    private fun generateAnagram(tvScrambled: TextView) {
        correctWord = wordList.random()
        scrambledWord = correctWord.toList().shuffled().joinToString("")
        tvScrambled.text = scrambledWord

        // Limpia el campo de texto de la entrada del usuario
        clearUserInput()
    }

    private fun clearUserInput() {
        val etUserInput = findViewById<TextView>(R.id.etUserInput)
        etUserInput.text = ""
    }

    private fun findDefinition(word: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.dictionaryapi.dev/") // Cambia por la URL base de tu API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val dictionaryApiService = retrofit.create(DictionaryApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = dictionaryApiService.getDefinition(word)

                withContext(Dispatchers.Main) {
                    val definition = response.meanings
                        .flatMap { it.definitions }
                        .firstOrNull()?.definition

                    // Actualiza la UI con la definición
                    findViewById<TextView>(R.id.tvResult).text = definition ?: "No se encontró definición."

                    // Genera un nuevo anagrama
                    generateAnagram(findViewById<TextView>(R.id.tvScrambledWord))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    findViewById<TextView>(R.id.tvResult).text = ""

                    // Genera un nuevo anagrama incluso si hay un error
                    generateAnagram(findViewById<TextView>(R.id.tvScrambledWord))
                }
            }
        }
    }
}

