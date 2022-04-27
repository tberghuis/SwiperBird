package xyz.tberghuis.swiperbird.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import xyz.tberghuis.swiperbird.composables.PagerContainer
import xyz.tberghuis.swiperbird.viewmodels.SharedViewModel
import xyz.tberghuis.swiperbird.viewmodels.SwiperViewModel

@Composable
fun SwiperScreen(navController: NavController, sharedViewModel: SharedViewModel) {

  val swiperViewModel: SwiperViewModel = hiltViewModel()
  LaunchedEffect(Unit) {
    swiperViewModel.searchTweets(sharedViewModel.searchTerm)
  }

  PagerContainer()
}