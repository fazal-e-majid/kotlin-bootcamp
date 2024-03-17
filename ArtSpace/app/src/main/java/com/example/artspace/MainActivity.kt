package com.example.artspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artspace.ui.theme.ArtSpaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeView()
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeView() {
    val artworks = listOf(
        Artwork(R.drawable.artwork_1,
            "Color Study. Squares with Concentric Circles",
            "Wassily Kandinsky",
            "1913"),
        Artwork(R.drawable.artwork_3,
            "Harlequin’s Carnival",
            "Joan Miró",
            "1924"),
        Artwork(R.drawable.artwork_2,
            "Autumn Rhythm (Number 30),",
            "Jackson Pollock",
            "1948"),
    )
    var selectedIndex by remember { mutableStateOf(0) }
    val art = artworks[selectedIndex]

    ArtSpaceTheme {
        // A surface container using the 'background' color from the theme
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .padding(top = 40.dp),
                shadowElevation = 8.dp
            ) {
                Image(
                    painter = painterResource(id = art.id), null,
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column {
                Column(
                    modifier = Modifier
                        .padding()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 12.dp),
                ) {
                    Text(text = art.title, fontSize = 20.sp, color = Color(R.color.gray_text))
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(text = art.artist, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = " (${art.year})", fontSize = 16.sp, color = Color(R.color.gray_text))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        selectedIndex = previousArtwork(selectedIndex, artworks.size)
                    }, modifier = Modifier.weight(1f), enabled = (selectedIndex != 0)) {
                        Text(text = "Previous")
                    }
                    Spacer(Modifier.weight(1f))
                    Button(onClick = {
                        selectedIndex = nextArtwork(selectedIndex, artworks.size)
                    }, modifier = Modifier.weight(1f), enabled = (selectedIndex != artworks.size-1)) {
                        Text(text = "Next")
                    }
                }
            }
        }
    }
}

private fun nextArtwork(selectedIndex: Int, size: Int): Int {
    val next = selectedIndex + 1
    return if (next > size-1) 0 else next
}

private fun previousArtwork(selectedIndex: Int, size: Int): Int {
    val previous = selectedIndex - 1
    return if (previous < 0) size-1 else previous
}

data class Artwork (
    @DrawableRes val id: Int,
    val title: String,
    val artist: String = "",
    val year: String = ""
)