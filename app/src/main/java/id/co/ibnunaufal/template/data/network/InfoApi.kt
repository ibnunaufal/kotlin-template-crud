package id.co.ibnunaufal.template.data.network

import id.co.ibnunaufal.template.data.model.InfoModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface InfoApi {
    @Headers("Content-Type: application/json")
    @GET("info/info_versi_mobile_all/")
    fun getInfo(): Call<InfoModel>
}