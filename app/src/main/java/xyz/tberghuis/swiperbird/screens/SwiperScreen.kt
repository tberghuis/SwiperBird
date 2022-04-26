package xyz.tberghuis.swiperbird.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import xyz.tberghuis.swiperbird.viewmodels.SharedViewModel


@Composable
fun SwiperScreen(navController: NavController, sharedViewModel: SharedViewModel) {
  Text("hello swiper screen ${sharedViewModel.searchTerm}")
}