package xyz.tberghuis.swiperbird.tmp

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryName
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

  data class SearchMetadata(val next_results: String)

}


interface TwitterApi {
//  @GET("1.1/search/tweets.json?q=puppy%20filter%3Anative_video%20-filter:retweets")
//  fun searchTweets(): Call<SearchResponse>

  @GET("1.1/search/tweets.json")
  fun searchTweets(@Query("q") query: String): Call<SearchResponse>

  @GET
  fun get(@Url url: String): Call<SearchResponse>

  // is there a way to do this???
  // doitwrong for now
//  fun moreTweets(query: String): Call<SearchResponse> {
//    return get("1.1/search/tweets.json$query")
//  }

}


object RetrofitInstance {

  val api: TwitterApi by lazy {

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
//      .addConverterFactory(MoshiConverterFactory.create())
      // TODO use moshi somehow instead
      .addConverterFactory(MoshiConverterFactory.create())
      .client(okHttpClient)
      .build()
      .create(TwitterApi::class.java)
  }
}