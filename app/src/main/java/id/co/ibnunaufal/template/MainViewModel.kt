package id.co.ibnunaufal.template

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.co.ibnunaufal.template.data.network.Resource
import id.co.ibnunaufal.template.data.repository.UserRepository
import id.co.ibnunaufal.template.data.response.LoginResponse
import id.co.ibnunaufal.template.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository): BaseViewModel(repository) {

    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse
    fun login(
        username: String,
        password: String
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(username, password)
    }
}