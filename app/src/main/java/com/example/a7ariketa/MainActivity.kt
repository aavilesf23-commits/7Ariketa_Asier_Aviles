package com.example.a7ariketa

import android.content.Context
import android.os.Bundle
import org.json.JSONArray
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a7ariketa.ui.theme._7AriketaTheme
import androidx.compose.ui.tooling.preview.Preview

// 1. Data Class para mapear los objetos del JSON
data class Bideojokoa(
    val id: Int,
    val titulua: String,
    val mota: String,
    val plataforma: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _7AriketaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BideojokoakApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// 2. Función para leer y parsear el archivo JSON desde assets

// Función de lectura sin usar Gson (nativa de Android)
fun readJsonFromAssets(context: Context, fileName: String): List<Bideojokoa> {
    val gameList = mutableListOf<Bideojokoa>()
    try {
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            gameList.add(
                Bideojokoa(
                    id = item.getInt("id"),
                    titulua = item.getString("titulua"),
                    mota = item.getString("mota"),
                    plataforma = item.getString("plataforma")
                )
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return gameList
}

// 3. Composable principal
@Composable
fun BideojokoakApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var gameList by remember { mutableStateOf<List<Bideojokoa>>(emptyList()) }

    Box(modifier = modifier.fillMaxSize()) {
        // Imagen de Fondo
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título principal
            Text(
                text = "Bideojokoak",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Botón para cargar datos JSON al pulsar clic
            Button(
                onClick = {
                    gameList = readJsonFromAssets(context, "bideojokoak.json")
                },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(text = "Irakurri JSON Datuak")
            }

            // Lista optimizada usando LazyColumn y Cards
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(gameList) { game ->
                    BideojokoCard(game = game)
                }
            }
        }
    }
}

// 4. Composable para cada tarjeta de videojuego
@Composable
fun BideojokoCard(game: Bideojokoa) {
    // Asignación de imagen según el ID del juego procedente del JSON
    val imageRes = when (game.id) {
        1 -> R.drawable.zelda
        2 -> R.drawable.elden_ring
        3 -> R.drawable.cyberpunk
        4 -> R.drawable.hollow_knight
        else -> R.drawable.ic_launcher_background
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = game.titulua,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = game.titulua,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Mota: ${game.mota}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Plataforma: ${game.plataforma}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    _7AriketaTheme {
        BideojokoakApp()
    }
}