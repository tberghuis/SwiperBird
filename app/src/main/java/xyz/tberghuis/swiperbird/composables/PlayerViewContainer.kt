package xyz.tberghuis.swiperbird.composables

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import xyz.tberghuis.swiperbird.databinding.PlayerViewBinding

@Composable
fun PlayerViewContainer(player: Player, videoUrl: String) {
  // yes i need to construct a fresh view in factory

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