package xyz.tberghuis.swiperbird.composables

import android.view.LayoutInflater
import android.view.TextureView
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerControlView

import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.MimeTypes
import xyz.tberghuis.swiperbird.databinding.PlayerViewBinding
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
    PlayerViewContainer(player, videoUrl)
  }
}

@Composable
fun PlayerViewContainer(player: Player, videoUrl: String) {
  // yes i need to construct a fresh view in factory
  // factory is stateful

  LaunchedEffect(Unit) {
    player.setMediaItem(MediaItem.fromUri(videoUrl))
    // do i need prepare
    player.prepare()
  }

  // AndroidViewBinding needed to use xml to set surface_type to texture_view
  AndroidViewBinding({ inflater: LayoutInflater, _: ViewGroup, _: Boolean ->
    val viewBinding = PlayerViewBinding.inflate(inflater)
    viewBinding.videoView.player = player
    viewBinding
  })
}