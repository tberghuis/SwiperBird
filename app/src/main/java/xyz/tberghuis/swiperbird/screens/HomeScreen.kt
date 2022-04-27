package xyz.tberghuis.swiperbird.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import xyz.tberghuis.swiperbird.util.logd
import xyz.tberghuis.swiperbird.viewmodels.SharedViewModel

@Composable
fun HomeScreen(navController: NavController, sharedViewModel: SharedViewModel) {
  val textState = remember { mutableStateOf(TextFieldValue()) }

  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text("Swiper Bird", fontSize = 40.sp)
    Spacer(Modifier.height(40.dp))
    TextField(
      value = textState.value,
      onValueChange = { textState.value = it }
    )
    Spacer(Modifier.height(10.dp))
    Button(onClick = {
      logd("nav")
      sharedViewModel.searchTerm = textState.value.text
      navController.navigate("swiper")
    }) {
      Text("Search Tweets")
    }
  }
}