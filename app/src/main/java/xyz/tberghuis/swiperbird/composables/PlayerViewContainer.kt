package xyz.tberghuis.swiperbird.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.MimeTypes
import xyz.tberghuis.swiperbird.util.logd


@Composable
fun PlayerViewContainerContainer(videoUrl: String) {
  val context = LocalContext.current

  // todo array of 3 player and playerview

  val player = remember {
    ExoPlayer.Builder(context).build().apply {
      playWhenReady = false
      repeatMode = Player.REPEAT_MODE_ONE
    }
  }


  PlayerViewContainer(player, videoUrl)

}


@Composable
fun PlayerViewContainer(player: Player, videoUrl: String) {

  // yes i need to construct a fresh view in factory
  // factory is stateful

  AndroidView({
    StyledPlayerView(it).apply {
      useController = false
      this.player = player
    }
  }) {
    logd("update ran")
  }

  LaunchedEffect(Unit) {
    logd("PlayerViewContainer LaunchedEffect")

    player.setMediaItem(MediaItem.fromUri(videoUrl))
    // do i need prepare
    player.prepare()
    player.play()
  }

}