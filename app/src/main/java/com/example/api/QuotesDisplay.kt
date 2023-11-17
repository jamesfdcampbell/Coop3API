package com.example.api

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.api.ui.theme.comfortaa

@Composable
fun QuotesDisplay(quotes: List<Quote>?, onNewQuoteClicked: () -> Unit) {
    val backgroundPainter = painterResource(id = R.drawable.background)
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = backgroundPainter,
            contentDescription = "Background image",
            contentScale = ContentScale.Crop, // or ContentScale.FillBounds etc. based on your requirement
            modifier = Modifier.matchParentSize()
        )
        // Your column with text and button goes here, as previously defined.
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color.Black.copy(alpha = 0.2f) // Use a transparent color for the Surface
        ) {
            Surface(
                modifier = Modifier
                    .padding(16.dp),
                color = Color.Transparent
            ) {
                if (quotes != null && quotes.isNotEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(bottom = 155.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "\"${quotes.first().quote}\"",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White,
                                fontFamily = comfortaa,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "- ${quotes.first().author}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White,
                                fontFamily = comfortaa,
                                textAlign = TextAlign.Center
                            )
                        }
                        Button(
                            onClick = onNewQuoteClicked,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006f83)),
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 30.dp)// Adjust the value as needed
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
}