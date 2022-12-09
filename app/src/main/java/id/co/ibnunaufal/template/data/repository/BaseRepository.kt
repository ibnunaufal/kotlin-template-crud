package id.co.ibnunaufal.template.data.repository

import id.co.ibnunaufal.template.data.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {
    suspend fun <T> safeApiCall(
        apiCall :suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO){
            try {
                Resource.Success(apiCall.invoke())
            }catch (throwable: Throwable){
                when(throwable){
                    is  HttpException ->{
                        Resource.Failure(isNetworkError = false,throwable.code(),throwable.response()?.errorBody())
                    }else -> {
                    Resource.Failure(isNetworkError = true,errorCode = null,errorBody = null)
                }
                }
            }
        }
    }
    suspend fun <T> safeApiCallWithTimeout(
        apiCall :suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO){
            try {
                Resource.Success(apiCall.invoke())
            }catch (throwable: Throwable){
                when(throwable){
                    is  HttpException ->{
                        Resource.Failure(isNetworkError = false,throwable.code(),throwable.response()?.errorBody())
                    }else -> {
                    Resource.Failure(isNetworkError = true,errorCode = null,errorBody = null)
                }
                }
            }
        }
    }
}