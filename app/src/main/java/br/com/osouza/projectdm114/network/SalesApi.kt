package br.com.osouza.projectdm114.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://sales-provider.appspot.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .addInterceptor(OauthTokenInterceptor())
    .authenticator(OauthTokenAuthenticator())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(okHttpClient)
    .build()


interface SalesApiService {
    @GET("api/products")
    fun getProducts(): Deferred<List<Product>>

    @GET("api/products/{code}")
    fun getProductByCode(@Path("code") code: String): Deferred<Product>

    @POST("oauth/token")
    @FormUrlEncoded
    fun getToken(
        @Header("Authorization") basicAuthentication: String,
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<OauthTokenResponse>
}

object SalesApi {
    val retrofitService: SalesApiService by lazy {
        retrofit.create(SalesApiService::class.java)
    }
}