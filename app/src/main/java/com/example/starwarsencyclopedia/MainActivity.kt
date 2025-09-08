package com.example.starwarsencyclopedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private val characterList = mutableListOf<Character>()
    private lateinit var adapter: CharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        adapter = CharacterAdapter(this, characterList)
        listView.adapter = adapter

        fetchCharacters()

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if (position < characterList.size) {
                val character = characterList[position]

                val fragmentContainer = findViewById<FrameLayout>(R.id.fragmentContainer)

                if (fragmentContainer == null) {
                    Intent(this, EmptyActivity::class.java).apply {
                        putExtra("character", character)
                        startActivity(this)
                    }
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, DetailsFragment.newInstance(character))
                        .commit()
                }
            }
        }
    }

    private fun fetchCharacters() {
        Toast.makeText(this, "Loading characters...", Toast.LENGTH_SHORT).show()

        lifecycleScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    try {
                        // Try multiple endpoints in case one is down
                        val endpoints = listOf(
                            "https://swapi.dev/api/people/",
                            "https://swapi.py4e.com/api/people/",
                            "https://swapi.info/api/people/"
                        )

                        var response: String? = null
                        for (endpoint in endpoints) {
                            try {
                                val url = URL(endpoint)
                                val connection = url.openConnection() as HttpURLConnection
                                connection.requestMethod = "GET"
                                connection.connectTimeout = 15000
                                connection.readTimeout = 15000

                                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                                    val reader = connection.inputStream.bufferedReader()
                                    response = reader.use { it.readText() }
                                    reader.close()
                                    break // Success, break out of loop
                                }
                            } catch (e: Exception) {
                                continue // Try next endpoint
                            }
                        }
                        response
                    } catch (e: Exception) {
                        null
                    }
                }

                if (result != null) {
                    parseCharacters(result)
                    Toast.makeText(this@MainActivity, "Characters loaded successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    loadTestData()
                }
            } catch (e: Exception) {
                loadTestData()
            }
        }
    }

    private fun parseCharacters(jsonString: String) {
        try {
            val jsonObject = JSONObject(jsonString)
            val charactersArray = jsonObject.getJSONArray("results")

            characterList.clear()
            for (i in 0 until charactersArray.length()) {
                val characterJson = charactersArray.getJSONObject(i)
                characterList.add(Character(
                    name = characterJson.optString("name", "Unknown"),
                    height = characterJson.optString("height", "Unknown"),
                    mass = characterJson.optString("mass", "Unknown"),
                    hairColor = characterJson.optString("hair_color", "Unknown"),
                    skinColor = characterJson.optString("skin_color", "Unknown"),
                    eyeColor = characterJson.optString("eye_color", "Unknown"),
                    birthYear = characterJson.optString("birth_year", "Unknown"),
                    gender = characterJson.optString("gender", "Unknown")
                ))
            }

            runOnUiThread {
                adapter.notifyDataSetChanged()
                if (characterList.isEmpty()) {
                    loadTestData()
                }
            }
        } catch (e: Exception) {
            loadTestData()
        }
    }

    private fun loadTestData() {
        characterList.clear()
        characterList.add(Character("Luke Skywalker", "172", "77", "blond", "fair", "blue", "19BBY", "male"))
        characterList.add(Character("Darth Vader", "202", "136", "none", "white", "yellow", "41.9BBY", "male"))
        characterList.add(Character("Leia Organa", "150", "49", "brown", "light", "brown", "19BBY", "female"))
        characterList.add(Character("Obi-Wan Kenobi", "182", "77", "auburn, white", "fair", "blue-gray", "57BBY", "male"))
        characterList.add(Character("Yoda", "66", "17", "white", "green", "brown", "896BBY", "male"))
        characterList.add(Character("R2-D2", "96", "32", "n/a", "white, blue", "red", "33BBY", "n/a"))
        characterList.add(Character("C-3PO", "167", "75", "n/a", "gold", "yellow", "112BBY", "n/a"))
        characterList.add(Character("Han Solo", "180", "80", "brown", "fair", "brown", "29BBY", "male"))

        runOnUiThread {
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Using test data (API unavailable)", Toast.LENGTH_LONG).show()
        }
    }
}