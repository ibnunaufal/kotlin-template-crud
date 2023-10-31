package id.co.ibnunaufal.template.data.network

import id.co.ibnunaufal.template.data.model.InfoModel
import id.co.ibnunaufal.template.data.model.PostInfoModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface InfoApi {
    @Headers("Content-Type: application/json")
    @GET("info/info_versi_mobile_all")
    fun getInfo(): Call<List<InfoModel>>

    @Headers("Content-Type: application/json")
    @POST("info/info_versi_mobile")
    fun postInfo(@Body info: PostInfoModel): Call<InfoModel>

    @Headers("Content-Type: application/json")
    @DELETE("info/info_versi_mobile/{id}")
    fun deleteInfo(@Path("id") id : String ) : Call<InfoModel>

    @Headers("Content-Type: application/json")
    @PUT("info/info_versi_mobile/{id}")
    fun editInfo(@Path("id") id: String, @Body info: InfoModel): Call<InfoModel>
}