package xyz.tberghuis.swiperbird.tmp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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

  private val _videoUrls = mutableStateListOf<String>()
  val videoUrls: List<String> = _videoUrls


  fun searchTweets() {


    // correct dispatcher for network???
    viewModelScope.launch(Dispatchers.IO) {

      // todo try catches, no network, server down etc
      val res = RetrofitInstance.api.searchTweets().execute()
      val body = res.body()
      if (body == null) {
        // todo tell user somehow
        return@launch
      }

      val urls = body.statuses.map { status ->
        // is there a better way???
        val variants = status.extended_entities?.let {
          // does media always have at least 1 element???
          it.media[0].video_info.variants
        }

        val variant = variants?.firstOrNull { it.content_type == "application/x-mpegURL" }
        variant?.url
      }.filterNotNull()

      logd(urls.toString())
      // todo emit urls to shared flow with event type, received more videos

      _videoUrls.addAll(urls)
    }


  }

  fun clear(){
    _videoUrls.clear()
  }

}


@Composable
fun PagingDemo() {
//  Text("hello paging demo")

  val viewModel: PagingViewModel = hiltViewModel()

  val textState = remember { mutableStateOf(TextFieldValue()) }

  LazyColumn {
    item {
      TextField(
        value = textState.value,
        onValueChange = { textState.value = it }
      )
    }
    item {
      Row {
        Button(onClick = {
          logd("search")
          viewModel.searchTweets()
        }) {
          Text("search")
        }

        Button(onClick = {
          viewModel.clear()
        }) {
          Text("clear")
        }

      }
    }
    items(items = viewModel.videoUrls) { videoUrl ->
      Text(videoUrl)
    }
  }
}