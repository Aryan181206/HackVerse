package com.example.hackverse

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hackverse.databinding.ActivityMainScreen1Binding

class MainScreen1 : AppCompatActivity() {

    lateinit var binding: ActivityMainScreen1Binding    //find view wale ko hatane wala code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainScreen1Binding.inflate(layoutInflater)                 //find view wale ko hatane wala code
        setContentView(binding.root)                                                 //find view wale ko hatane wala code
        setContentView(R.layout.activity_main_screen1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.textView2.setOnClickListener {  }
    }
}