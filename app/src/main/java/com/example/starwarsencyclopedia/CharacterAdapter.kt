package com.example.starwarsencyclopedia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CharacterAdapter(private val context: Context, private val characters: List<Character>) : BaseAdapter() {

    override fun getCount(): Int = characters.size

    override fun getItem(position: Int): Any = characters[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = characters[position].name

        return view
    }
}