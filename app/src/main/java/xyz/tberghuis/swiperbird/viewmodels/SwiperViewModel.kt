package xyz.tberghuis.swiperbird.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import xyz.tberghuis.swiperbird.data.SearchResponse
import xyz.tberghuis.swiperbird.data.TwitterApiWrapper
import javax.inject.Inject


@HiltViewModel
class SwiperViewModel @Inject constructor(
) : ViewModel() {
  private val _videoUrls = mutableStateListOf<String>()
  val videoUrls: List<String> = _videoUrls
  private var nextResults: String? = null

  var initialFetch = false

  init {
    // should i kick this of from view layer
    // yes because I need reference to sharedViewModel.searchTerm
//    searchTweets()
  }


  fun searchTweets(searchTerm: String) {
    // TODO why am i still getting duplicate results
    // last resort, manual filter duplicates

    if (initialFetch) {
      return
    }
    initialFetch = true
    val call = TwitterApiWrapper.searchTweets(searchTerm)
    executeSearchCall(call)
  }

  fun clear() {
    _videoUrls.clear()
  }

  var isFetchingMore = false
  fun fetchMore() {
    if (isFetchingMore || nextResults == null) return
    isFetchingMore = true
    val call = TwitterApiWrapper.fetchNextResults(nextResults!!)
    executeSearchCall(call)
    isFetchingMore = false
  }

  private fun executeSearchCall(call: Call<SearchResponse>) {
    // is IO the right scope for network?
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
