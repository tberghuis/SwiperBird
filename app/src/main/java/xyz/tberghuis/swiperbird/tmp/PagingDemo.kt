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
import retrofit2.Call
import xyz.tberghuis.swiperbird.util.logd
import javax.inject.Inject


@HiltViewModel
class PagingViewModel @Inject constructor(
) : ViewModel() {
  val willitblend = "hello vm"

  private val _videoUrls = mutableStateListOf<String>()
  val videoUrls: List<String> = _videoUrls

  private var nextResults = ""

  fun searchTweets() {
    // TODO why am i still getting duplicate results
    // last resort, manual filter duplicates

//    val query = "puppy filter:native_video -filter:retweets"
//    val call = RetrofitInstance.api.searchTweets(query)

    val call = RetrofitInstance.searchTweets("#surfing")
    executeSearchCall(call)
  }

  fun clear() {
    _videoUrls.clear()
  }

  fun fetchMore() {
    // doingitwrong
//    val call = RetrofitInstance.api.get("1.1/search/tweets.json$nextResults")

    val call = RetrofitInstance.fetchNextResults(nextResults)
    executeSearchCall(call)
  }

  private fun executeSearchCall(call: Call<SearchResponse>) {
    viewModelScope.launch(Dispatchers.IO) {
      // todo try catches, no network, server down etc
      val res = call.execute()
      val body = res.body()
      if (body == null) {
        // todo tell user somehow
        return@launch
      }

      val urls = body.statuses.mapNotNull { status ->
        val variants = status.extended_entities?.let {
          // does media always have at least 1 element???
          it.media[0].video_info.variants
        }
        val variant = variants?.firstOrNull { it.content_type == "application/x-mpegURL" }
        variant?.url
      }
      _videoUrls.addAll(urls)
      nextResults = body.search_metadata.next_results
    }
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

        Button(onClick = {
          viewModel.fetchMore()
        }) {
          Text("fetch more")
        }


      }
    }
    items(items = viewModel.videoUrls) { videoUrl ->
      Text(videoUrl)
    }
  }
}