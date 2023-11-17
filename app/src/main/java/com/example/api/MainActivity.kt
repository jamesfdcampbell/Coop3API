package com.example.api

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.api.ui.theme.APITheme
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            APITheme {
                val quoteViewModel: QuoteViewModel = viewModel()
                val quotes = quoteViewModel.quotes.value
                QuotesDisplay(quotes = quotes)
            }
        }
    }
}



object RetrofitInstance {
    private const val BASE_URL = "https://api.api-ninjas.com"

    private fun getRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("X-Api-Key", "wI+itsJhULsHnoh3x5ov3Q==zzY9UsNY3QDmY1ge") // Replace with your actual API key
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: QuoteApi by lazy {
        getRetrofit().create(QuoteApi::class.java)
    }
}

interface QuoteApi {
    @GET("/v1/quotes")
    suspend fun getRandomQuote(@Query("category") category: String?): List<Quote>
}

// Simplify the ViewModel by removing the API key parameter
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
                // Handle the error state
            }
        }
    }
}


@Composable
fun QuotesDisplay(quotes: List<Quote>?) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (quotes != null && quotes.isNotEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Iterate over the quotes and display them
                    quotes.forEach { quote ->
                        Text(
                            text = "\"${quote.quote}\"",
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "- ${quote.author}",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp)) // Space between each quote
                    }
                }
            }
        } else {
            // Display a loading animation or a placeholder
            CircularProgressIndicator()
        }
    }
}