package com.example.juegopalabras

import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class SopaDeLetrasActivity : AppCompatActivity() {

    private val wordList = listOf("ANIMAL", "PERRO", "GATO", "LUZ", "ESTRELLA")
    private val gridSize = 14
    private lateinit var gridSopaLetras: GridLayout
    private var selectedLetters = mutableListOf<TextView>()
    private var wordColors = mutableMapOf<String, Int>()
    private val availableColors = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN)
    private var currentWord = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sopa_letras)

        gridSopaLetras = findViewById(R.id.gridSopaLetras)
        createSopaDeLetras()
    }

    private fun createSopaDeLetras() {
        gridSopaLetras.rowCount = gridSize
        gridSopaLetras.columnCount = gridSize

        val emptyGrid = Array(gridSize) { Array(gridSize) { "" } }
        val letters = ('A'..'Z').toList()

        // Agrega palabras a la cuadrícula, asegurando que no se superpongan
        for (word in wordList) {
            var placed = false
            while (!placed) {
                placed = placeWordInGrid(word, emptyGrid)
            }
        }

        // Rellena las celdas vacías con letras aleatorias
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                if (emptyGrid[i][j].isEmpty()) {
                    emptyGrid[i][j] = letters.random().toString()
                }
            }
        }

        // Muestra la cuadrícula en la interfaz de usuario
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                val letterView = TextView(this)
                letterView.text = emptyGrid[i][j]
                letterView.textSize = 20f
                letterView.setPadding(8, 8, 8, 8)
                letterView.setBackgroundColor(Color.TRANSPARENT)

                // Añadir Touch Listener para manejar arrastre
                letterView.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_DOWN) {
                        if (!selectedLetters.contains(letterView)) {
                            handleLetterTouch(letterView)
                        }
                    }
                    true
                }

                gridSopaLetras.addView(letterView)
            }
        }
    }

    // Verifica que el espacio esté disponible antes de colocar la palabra
    private fun placeWordInGrid(word: String, grid: Array<Array<String>>): Boolean {
        val random = Random
        val direction = random.nextInt(2) // 0 para horizontal, 1 para vertical

        val row = random.nextInt(gridSize)
        val col = random.nextInt(gridSize)

        if (direction == 0) { // Horizontal
            if (col + word.length <= gridSize) {
                for (i in word.indices) {
                    if (grid[row][col + i].isNotEmpty()) return false
                }
                for (i in word.indices) {
                    grid[row][col + i] = word[i].toString()
                }
                return true
            }
        } else { // Vertical
            if (row + word.length <= gridSize) {
                for (i in word.indices) {
                    if (grid[row + i][col].isNotEmpty()) return false
                }
                for (i in word.indices) {
                    grid[row + i][col] = word[i].toString()
                }
                return true
            }
        }
        return false
    }

    private fun handleLetterTouch(letterView: TextView) {
        // Resalta la letra seleccionada
        letterView.setBackgroundColor(Color.LTGRAY)
        letterView.setTextColor(Color.WHITE)

        // Agrega la letra a la lista de selección actual
        selectedLetters.add(letterView)

        // Construye la palabra a medida que el usuario selecciona letras
        currentWord.append(letterView.text.toString())

        // Verifica si se ha formado una palabra
        checkForWord()
    }

    private fun checkForWord() {
        val word = currentWord.toString()

        // Verifica si la palabra es válida
        if (wordList.contains(word)) {
            if (!wordColors.containsKey(word)) {
                // Asigna un color diferente a la palabra encontrada
                wordColors[word] = availableColors[wordColors.size % availableColors.size]
            }
            // Resalta la palabra completa en el color asignado
            highlightWord(wordColors[word]!!)
            // Marca la palabra como encontrada
            markWordAsFound(word)
            currentWord.clear()
        }
    }

    private fun highlightWord(color: Int) {
        for (letterView in selectedLetters) {
            letterView.setBackgroundColor(color)
            letterView.setTextColor(Color.WHITE)
        }
        selectedLetters.clear() // Limpiar las letras seleccionadas después de resaltar
    }

    private fun markWordAsFound(word: String) {
        // Actualiza la UI para mostrar que la palabra ha sido encontrada
        findViewById<TextView>(R.id.tvFoundWords).append("$word\n")
    }
}
