package id.co.ibnunaufal.template.data.network

import id.co.ibnunaufal.template.data.model.ModelLogin
import id.co.ibnunaufal.template.data.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserApi {

    @Headers("Content-Type: application/json")
    @POST("katalis/login")
    suspend fun login(
        @Body info: ModelLogin
    ): LoginResponse
}