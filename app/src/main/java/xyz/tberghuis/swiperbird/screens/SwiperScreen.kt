package xyz.tberghuis.swiperbird.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import xyz.tberghuis.swiperbird.composables.PlayerViewContainerContainer
import xyz.tberghuis.swiperbird.viewmodels.SharedViewModel
import xyz.tberghuis.swiperbird.viewmodels.SwiperViewModel


@Composable
fun SwiperScreen(navController: NavController, sharedViewModel: SharedViewModel) {
//  Text("hello swiper screen ${sharedViewModel.searchTerm}")

  val swiperViewModel: SwiperViewModel = hiltViewModel()
  LaunchedEffect(Unit) {
    swiperViewModel.searchTweets(sharedViewModel.searchTerm)
  }

//  LazyColumn {
//    items(items = swiperViewModel.videoUrls) { videoUrl ->
//      Text(videoUrl)
//    }
//  }

  if (swiperViewModel.videoUrls.isNotEmpty()) {
    PlayerViewContainerContainer(swiperViewModel.videoUrls[0])
  }

}