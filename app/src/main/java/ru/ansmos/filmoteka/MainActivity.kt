package ru.ansmos.filmoteka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initButtons()
    }

    fun initButtons(){
        val buttonLayout = findViewById<LinearLayout>(R.id.button_container)
        for (i in 0..buttonLayout.childCount - 1){
            val v = buttonLayout.get(i)
            if (v is Button){
                v.setOnClickListener {
                    Toast.makeText(this, v.text, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}