package com.translator.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.translator.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.container,MainFragment())
            .addToBackStack(null).commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}