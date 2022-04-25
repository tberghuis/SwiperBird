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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.tberghuis.swiperbird.util.logd
import javax.inject.Inject


@HiltViewModel
class PagingViewModel @Inject constructor(
) : ViewModel() {
  val willitblend = "hello vm"

  fun searchTweets() {


    // correct dispatcher for network???
    viewModelScope.launch(Dispatchers.IO) {
      val res = RetrofitInstance.api.searchTweets().execute()
      logd(res.toString())

      val body = res.body()
      logd(body.toString())
    }

  }

}


@Composable
fun PagingDemo() {
//  Text("hello paging demo")

  val viewModel: PagingViewModel = hiltViewModel()

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
        viewModel.searchTweets()
      }) {
        Text("search")
      }
    }
    Row {
      Text("hello paging demo ${viewModel.willitblend}")
    }

  }
}