package com.example.a2048

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)


        if(currentFragment == null){
            val gameFragment = GameFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container,gameFragment).commit()
        }
    }
}