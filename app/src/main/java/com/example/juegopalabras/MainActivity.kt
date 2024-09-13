package com.example.juegopalabras

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnAnagramas).setOnClickListener {
            startActivity(Intent(this, AnagramActivity::class.java))
        }

        findViewById<Button>(R.id.btnSopaDeLetras).setOnClickListener {
            startActivity(Intent(this, SopaDeLetrasActivity::class.java))
        }
    }
}
