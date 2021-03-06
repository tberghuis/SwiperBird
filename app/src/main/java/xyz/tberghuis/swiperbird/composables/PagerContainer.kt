package xyz.tberghuis.swiperbird.composables

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import xyz.tberghuis.swiperbird.viewmodels.SwiperViewModel
import kotlin.math.absoluteValue

fun buildPlayer(context: Context): Player {
  return ExoPlayer.Builder(context).build().apply {
    playWhenReady = false
    repeatMode = Player.REPEAT_MODE_ONE
  }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerContainer() {
  val swiperViewModel: SwiperViewModel = hiltViewModel()
  val pagerState = rememberPagerState()
  val context = LocalContext.current
  val players = remember {
    // probably a better way of writing this
    val p0 = buildPlayer(context)
    val p1 = buildPlayer(context)
    val p2 = buildPlayer(context)
    arrayOf(p0, p1, p2)
  }

  DisposableEffect(Unit) {
    onDispose {
      players[0].release()
      players[1].release()
      players[2].release()
    }
  }

  LaunchedEffect(pagerState.currentPage) {
    // is 2 a good number???
    if (swiperViewModel.videoUrls.size - pagerState.currentPage <= 2) {
      swiperViewModel.fetchMore()
    }
  }

  VerticalPager(count = swiperViewModel.videoUrls.size, state = pagerState) { page ->
    val player = players[page % 3]

    if ((page - currentPage).absoluteValue > 1) {
      return@VerticalPager
    }

    LaunchedEffect(currentPage) {
      if (currentPage == page) {
        player.play()
      } else {
        player.pause()
      }
    }

    Box(contentAlignment = Alignment.Center,
      modifier = Modifier
        .fillMaxSize()
        .clickable {
          if (player.isPlaying) {
            player.pause()
          } else {
            player.play()
          }
        }
    ) {
      PlayerViewContainer(player, swiperViewModel.videoUrls[page])
    }
  }
}