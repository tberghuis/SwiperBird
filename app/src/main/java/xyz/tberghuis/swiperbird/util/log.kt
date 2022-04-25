package xyz.tberghuis.swiperbird.util

import android.util.Log
import xyz.tberghuis.swiperbird.BuildConfig.DEBUG

fun logd(s: String) {
  if (DEBUG) Log.d("xxx", s)
}