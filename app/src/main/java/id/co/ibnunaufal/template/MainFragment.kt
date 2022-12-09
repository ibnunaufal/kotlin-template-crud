package id.co.ibnunaufal.template

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import id.co.ibnunaufal.template.data.network.Resource
import id.co.ibnunaufal.template.data.network.UserApi
import id.co.ibnunaufal.template.data.repository.UserRepository
import id.co.ibnunaufal.template.databinding.FragmentMainBinding
import id.co.ibnunaufal.template.ui.base.BaseFragment
import id.co.ibnunaufal.template.ui.handleApiError
import kotlinx.coroutines.runBlocking

class MainFragment: BaseFragment<MainViewModel, FragmentMainBinding, UserRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val uname = binding.edUsername.text.toString()
            val pass = binding.edPassword.text.toString()
            view?.let { progressDialog.show(it.context) }
            viewModel.login(uname, pass)
        }

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    progressDialog.dialog.dismiss()
                    val token = runBlocking { userPreferences.getAuthToken() }
                    Log.d("token",token.toString())
                }

                is Resource.Failure -> {
                    handleApiError(it)
                }
            }
        })
    }

    override fun getViewModel() = MainViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMainBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = UserRepository (
        remoteDataSource.buildApi(requireContext(), UserApi::class.java, userPreferences)
    )
}