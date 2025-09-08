package com.example.starwarsencyclopedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class DetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        arguments?.let { args ->
            view.findViewById<TextView>(R.id.textViewNameValue).text = args.getString("name", "Unknown")
            view.findViewById<TextView>(R.id.textViewHeightValue).text = args.getString("height", "Unknown")
            view.findViewById<TextView>(R.id.textViewMassValue).text = args.getString("mass", "Unknown")
            view.findViewById<TextView>(R.id.textViewHairColorValue).text = args.getString("hair_color", "Unknown")
            view.findViewById<TextView>(R.id.textViewSkinColorValue).text = args.getString("skin_color", "Unknown")
            view.findViewById<TextView>(R.id.textViewEyeColorValue).text = args.getString("eye_color", "Unknown")
            view.findViewById<TextView>(R.id.textViewBirthYearValue).text = args.getString("birth_year", "Unknown")
            view.findViewById<TextView>(R.id.textViewGenderValue).text = args.getString("gender", "Unknown")
        }

        return view
    }

    companion object {
        fun newInstance(character: Character): DetailsFragment {
            return DetailsFragment().apply {
                arguments = Bundle().apply {
                    putString("name", character.name)
                    putString("height", character.height)
                    putString("mass", character.mass)
                    putString("hair_color", character.hairColor)
                    putString("skin_color", character.skinColor)
                    putString("eye_color", character.eyeColor)
                    putString("birth_year", character.birthYear)
                    putString("gender", character.gender)
                }
            }
        }
    }
}