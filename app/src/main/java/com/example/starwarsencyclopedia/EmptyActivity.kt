package com.example.starwarsencyclopedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class EmptyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)

        val character = intent.getSerializableExtra("character") as? Character

        if (character != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, DetailsFragment.newInstance(character))
                .commit()
        } else {
            // If no character data, show error and close
            finish()
        }
    }
}