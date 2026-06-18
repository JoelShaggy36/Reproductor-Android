package com.shaggy.reproductor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shaggy.reproductor.ui.theme.ReproductorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReproductorTheme {
                ReproductorApp()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReproductorApp(){
    Scaffold(
        topBar = { CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text("Reproductor de Musica")
            },
            navigationIcon = {
                IconButton(onClick = {/*Do someting */}) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back")
                }
            },
            actions = {IconButton(onClick = {/*Do someting*/}) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = "Menu")
            } }
        ) },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                IconButton(onClick = { Destino.TODAS_LAS_CANCIONES}){
                    Image(
                        painter = painterResource(id = R.drawable.icon1),
                        contentDescription = "Imagen",
                        modifier = Modifier
                            .padding(10.dp)
                            .size(120.dp)
                            .clip(CircleShape)

                    )
                }
                IconButton(onClick = { Destino.REPRODUCTOR}){
                    Image(
                        painter = painterResource(id = R.drawable.icon2),
                        contentDescription = "Imagen",
                        modifier = Modifier
                            .padding(10.dp)
                            .size(520.dp)
                            .clip(CircleShape)

                    )
                }
                IconButton(onClick = { Destino.PLAYLIST}){
                    Image(
                        painter = painterResource(id = R.drawable.icon3),
                        contentDescription = "Imagen",
                        modifier = Modifier
                            .padding(10.dp)
                            .size(120.dp)
                            .clip(CircleShape)

                    )
                }

            }
        },
    ) {innerPadding ->
        var pantallaActual by rememberSaveable { mutableStateOf(Destino.TODAS_LAS_CANCIONES) }
        Column(
            modifier = Modifier.padding(innerPadding)
        ){
            when (pantallaActual){
                Destino.TODAS_LAS_CANCIONES -> CancionesScreen()
                Destino.CANCIONES_FAVORITAS -> Text("PANTALLA DE CANCIONES FAVORITAS")
                Destino.REPRODUCTOR -> Text("PANTALLA DEL REPRODUCTOR")
                Destino.PLAYLIST -> Text("PANTALLA DE PLAYLIST")
            }
        }
    }

}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    ReproductorTheme {
        ReproductorApp()
    }
}