package xyz.tberghuis.swiperbird.tmp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerDemo() {

  val pagerState = rememberPagerState()
  VerticalPager(count = 10, state = pagerState) { page ->
    // Our page content
    Text(
      text = "Page: $page",
      modifier = Modifier.fillMaxWidth()
    )
  }

}