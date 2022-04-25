package xyz.tberghuis.swiperbird

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import xyz.tberghuis.swiperbird.tmp.PagingDemo
import xyz.tberghuis.swiperbird.ui.theme.SwiperBirdTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      SwiperBirdTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
//          Greeting("Android")
//          VideoPlayerScreen()
          PagingDemo()
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String) {
  Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  SwiperBirdTheme {
    Greeting("Android")
  }
}


@Composable
fun VideoPlayerScreen() {
  val context = LocalContext.current
  var playWhenReady by remember { mutableStateOf(true) }
  val exoPlayer = remember {
    ExoPlayer.Builder(context).build().apply {
//      setMediaItem(MediaItem.fromUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
      setMediaItem(MediaItem.fromUri("https://video.twimg.com/ext_tw_video/1516279952999542785/pu/pl/m3jrOla2UtYumLXl.m3u8?tag=12&container=fmp4"))
      repeatMode = ExoPlayer.REPEAT_MODE_ALL
      playWhenReady = playWhenReady
      prepare()
      play()
    }
  }
  DisposableEffect(
    AndroidView(
      modifier = Modifier.fillMaxSize(),
      factory = {
        PlayerView(context).apply {
          player = exoPlayer
          useController = true
          FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
          )
        }
      }
    )
  ) {
    onDispose {
      exoPlayer.release()
    }
  }
}
