package com.example.api

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class QuoteViewModel: ViewModel() {
    private val _quotes = mutableStateOf<List<Quote>?>(null)
    val quotes: State<List<Quote>?> = _quotes

    init {
        fetchRandomQuote()
    }

    private fun fetchRandomQuote() {
        viewModelScope.launch {
            try {
                val fetchedQuotes = RetrofitInstance.api.getRandomQuote("inspirational")
                _quotes.value = fetchedQuotes
            } catch (e: Exception) {
                Log.e("QuoteViewModel", "Error fetching quotes", e)
            }
        }
    }

    fun newQuote() {
        fetchRandomQuote()
    }
}