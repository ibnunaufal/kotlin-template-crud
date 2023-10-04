package id.co.ibnunaufal.template.data.network

import id.co.ibnunaufal.template.data.model.ModelLogin
import id.co.ibnunaufal.template.data.response.AppDetailResponse
import id.co.ibnunaufal.template.data.response.LoginResponse
import retrofit2.http.*

interface UserApi {

    @Headers("Content-Type: application/json")
    @POST("katalis/login")
    suspend fun login(
        @Body info: ModelLogin
    ): LoginResponse

    @Headers("Content-Type: application/json")
    @GET("main_a/info/google-play/{packageName}")
    suspend fun cari(
        @Path("packageName") packageName: String
    ): AppDetailResponse
}