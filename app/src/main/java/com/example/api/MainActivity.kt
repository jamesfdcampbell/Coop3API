package com.example.api

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
                QuotesDisplay(quotes = quotes) {
                    quoteViewModel.newQuote()
                }
            }
        }
    }
}

//Custom font
val comfortaa = FontFamily(Font(R.font.comfortaa_regular))

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

@Composable
fun QuotesDisplay(quotes: List<Quote>?, onNewQuoteClicked: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            if (quotes != null && quotes.isNotEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "\"${quotes.first().quote}\"",
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = comfortaa,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "- ${quotes.first().author}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = comfortaa,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = onNewQuoteClicked,
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Load New Quote",
                            fontFamily = comfortaa)
                    }
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}
