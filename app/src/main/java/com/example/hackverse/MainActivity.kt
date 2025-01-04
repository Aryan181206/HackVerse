package com.example.hackverse

import DataStoreManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()
        val dataStoreManager = DataStoreManager(this)
        // Add a 3-second delay before checking user data
        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                val userdata = dataStoreManager.getUserData().first()
                val userId = userdata.third
                val intent = if (userId.isEmpty()) {
                    Intent(this@MainActivity, SignInScreen::class.java)
                } else {
                    Intent(this@MainActivity, MainScreen1::class.java)
                }
                startActivity(intent)
                finish()
            }
        }, 3000) // 3000ms = 3 seconds
    }
}