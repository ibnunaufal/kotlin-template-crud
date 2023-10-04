package id.co.ibnunaufal.template.data.repository

import id.co.ibnunaufal.template.data.model.ModelLogin
import id.co.ibnunaufal.template.data.network.UserApi

class UserRepository(private val api: UserApi) : BaseRepository(){
    suspend fun login(
        username: String,
        password: String,

        ) = safeApiCall {
        api.login(ModelLogin(username, password))
    }
    suspend fun cari(
        cari: String
        ) = safeApiCall {
        api.cari(cari)
    }
}