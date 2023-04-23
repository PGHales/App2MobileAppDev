package com.example.example35jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.example35jetpackcompose.ui.theme.Example35JetpackComposeTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    private val mWeatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory((application as WeatherApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Example35JetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MakeLayout()
                }
            }
        }
    }

    private fun loadWeatherData(location: String?) {
        //pass the location in to the view model
        mWeatherViewModel.setLocation(location!!)
    }

    /**
     * Makes the layout for the app using Jetpack Compose
     */
    @Composable
    fun MakeLayout() {
        var input by remember { mutableStateOf("") }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Enter location: ")
                TextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("City,Country") }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 0.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
            ) {
                Button(onClick = {
                    val sanitizedInput = input.replace(' ', '&')
                    loadWeatherData(sanitizedInput)
                },
                    modifier = Modifier.weight(1f)) {
                    Text(text = "Submit")
                }
            }

            val weatherData by mWeatherViewModel.data.collectAsState()
            if (weatherData != null) {
                Text("" + (weatherData!!.temperature.temp - 273.15).roundToInt() + " C")
                Text("" + weatherData!!.currentCondition.humidity + "%")
                Text("" + weatherData!!.currentCondition.pressure + " hPa")
            }
            MakeRecylerView(mWeatherViewModel.allCityWeather.collectAsState().value)
        }
    }

    @Composable
    fun MakeRecylerView(allCityWeather: List<WeatherTable>?) {
        if (allCityWeather != null) {
            for (city in allCityWeather) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = city.location,
                        modifier = Modifier.padding(end = 32.dp)
                    )
                    Text(text = "" + (JSONWeatherUtils.getWeatherData(city.weatherJson).temperature.temp - 273.15).roundToInt() + "C")
                }
            }
        }
    }
}