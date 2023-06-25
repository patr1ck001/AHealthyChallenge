package com.example.ahealthychallenge.presentation

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ahealthychallenge.databinding.ActivitySearchUserBinding

class SearchUserActivity : ComponentActivity() {

    private lateinit var searchText: EditText
    private lateinit var searchBtn: ImageButton
    private lateinit var searchList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchText = binding.searchText
        searchBtn = binding.searchBtn
        searchList = binding.searchList

        searchText.requestFocus()

        searchBtn.setOnClickListener{
            val searchTerm = searchText.text.toString()
            if(searchTerm.isEmpty()){
                searchText.setError("Invalid Username")
                return@setOnClickListener
            }
        }
    }
}