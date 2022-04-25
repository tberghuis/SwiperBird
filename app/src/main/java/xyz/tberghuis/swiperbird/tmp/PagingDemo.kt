package xyz.tberghuis.swiperbird.tmp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import xyz.tberghuis.swiperbird.util.logd

@Composable
fun PagingDemo() {
//  Text("hello paging demo")

  val textState = remember { mutableStateOf(TextFieldValue()) }

  Column {
    Row {
      TextField(
        value = textState.value,
        onValueChange = { textState.value = it }
      )
    }

    Row {
      Button(onClick = {
        logd("search")
      }) {
        Text("search")
      }
    }
    Row {
      Text("hello paging demo")
    }

  }
}