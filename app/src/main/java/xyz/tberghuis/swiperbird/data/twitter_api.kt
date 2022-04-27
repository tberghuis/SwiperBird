package xyz.tberghuis.swiperbird.data

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

data class SearchResponse(
  val statuses: List<Status>,
  val search_metadata: SearchMetadata
) {
  data class Status(val extended_entities: ExtendedEntities?)
  data class ExtendedEntities(val media: List<Media>)
  data class Media(
//    val media_url: String
    val video_info: VideoInfo
  )
  data class VideoInfo(val variants: List<Variant>)
  data class Variant(
    val bitrate: Int,
    val content_type: String,
    val url: String
  )
  data class SearchMetadata(val next_results: String?)
}

interface TwitterApi {
  @GET("1.1/search/tweets.json")
  fun searchTweets(@Query("q") query: String): Call<SearchResponse>

  @GET
  fun get(@Url url: String): Call<SearchResponse>
}

// need better naming
object TwitterApiWrapper {
  private val api: TwitterApi by lazy {
    val okHttpClient = OkHttpClient.Builder()
      .addInterceptor { chain ->
        val request = chain.request().newBuilder()
          // todo get from env var
          .addHeader(
            "authorization",
            "Bearer AAAAAAAAAAAAAAAAAAAAAHNIbwEAAAAAse%2BrpAsUqI4HGHVq0UruSViRhbg%3D6WvEmyb27xIscsg9mFWkvwajhqvrno1QG93UHWrG5d9hlaFgM6"
          )
        chain.proceed(request.build())
      }
      .build()

    Retrofit.Builder()
      .baseUrl("https://api.twitter.com")
      .addConverterFactory(MoshiConverterFactory.create())
      .client(okHttpClient)
      .build()
      .create(TwitterApi::class.java)
  }

  fun searchTweets(searchTerm: String): Call<SearchResponse> {
    val query = "$searchTerm filter:native_video -filter:retweets"
    return api.searchTweets(query)
  }

  fun fetchNextResults(nextResults: String): Call<SearchResponse> {
    return api.get("1.1/search/tweets.json$nextResults")
  }
}