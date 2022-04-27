package xyz.tberghuis.swiperbird

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import dagger.hilt.android.AndroidEntryPoint
import xyz.tberghuis.swiperbird.screens.HomeScreen
import xyz.tberghuis.swiperbird.screens.SwiperScreen
import xyz.tberghuis.swiperbird.tmp.PagerDemo
import xyz.tberghuis.swiperbird.ui.theme.SwiperBirdTheme
import xyz.tberghuis.swiperbird.util.logd
import xyz.tberghuis.swiperbird.viewmodels.SharedViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    logd("oncreate")

    setContent {
      SwiperBirdTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
//          App()
          PagerDemo()
        }
      }
    }
  }
}


@Composable
fun App() {

  logd("App render")

  val sharedViewModel: SharedViewModel = hiltViewModel()

  val navController = rememberNavController()
  NavHost(navController = navController, startDestination = "home") {
    composable("home") { HomeScreen(navController, sharedViewModel) }

    composable("swiper") { SwiperScreen(navController, sharedViewModel) }

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
