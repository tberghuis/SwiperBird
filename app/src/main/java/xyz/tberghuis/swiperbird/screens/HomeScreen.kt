package xyz.tberghuis.swiperbird.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import xyz.tberghuis.swiperbird.util.logd
import xyz.tberghuis.swiperbird.viewmodels.SharedViewModel

@Composable
fun HomeScreen(navController: NavController, sharedViewModel: SharedViewModel) {
  val textState = remember { mutableStateOf(TextFieldValue()) }

  Column {
    TextField(
      value = textState.value,
      onValueChange = { textState.value = it }
    )

    Button(onClick = {
      logd("nav")

      sharedViewModel.searchTerm = textState.value.text

// can i use special chars? probably not so using a shared VM
      navController.navigate("swiper")

    }) {
      Text("nav")
    }

  }
}