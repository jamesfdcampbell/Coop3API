package com.example.api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.api.ui.theme.APITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            APITheme {
                val quoteViewModel: QuoteViewModel = viewModel()
                val quotes = quoteViewModel.quotes.value
                QuotesDisplay(quotes = quotes) {
                    quoteViewModel.newQuote()
                }
            }
        }
    }
}