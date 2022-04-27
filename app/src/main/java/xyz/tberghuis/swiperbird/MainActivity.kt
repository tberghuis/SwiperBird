package xyz.tberghuis.swiperbird

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import xyz.tberghuis.swiperbird.screens.HomeScreen
import xyz.tberghuis.swiperbird.screens.SwiperScreen
import xyz.tberghuis.swiperbird.ui.theme.SwiperBirdTheme
import xyz.tberghuis.swiperbird.viewmodels.SharedViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      SwiperBirdTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          App()
        }
      }
    }
  }
}

@Composable
fun App() {
  val sharedViewModel: SharedViewModel = hiltViewModel()
  val navController = rememberNavController()

  NavHost(navController = navController, startDestination = "home") {
    composable("home") { HomeScreen(navController, sharedViewModel) }
    composable("swiper") { SwiperScreen(navController, sharedViewModel) }
  }
}