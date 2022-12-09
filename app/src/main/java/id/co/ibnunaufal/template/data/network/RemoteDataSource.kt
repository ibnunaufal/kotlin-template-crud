package id.co.ibnunaufal.template.data.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.BuildConfig
import id.co.ibnunaufal.template.data.preference.UserPreference
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {

    companion object {
        private const val BASE_URL = "https://api.dev.katalis.info/"

    }
    fun getBaseUrl(): String {
        return BASE_URL;
    }
    fun <Api> buildApi(
        context: Context,
        api: Class<Api>,
        pref: UserPreference
    ): Api {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(pref))
                    .also { client  ->
                        if (BuildConfig.DEBUG) {
                            val logging = HttpLoggingInterceptor()
                            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                            client.addInterceptor(logging)
                        }
                    }.addInterceptor(ChuckerInterceptor(context)).build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }
}