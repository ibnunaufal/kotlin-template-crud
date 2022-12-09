package id.co.ibnunaufal.template.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import id.co.ibnunaufal.template.MainActivity
import id.co.ibnunaufal.template.data.network.RemoteDataSource
import id.co.ibnunaufal.template.data.preference.UserPreference
import id.co.ibnunaufal.template.data.repository.BaseRepository
import id.co.ibnunaufal.template.ui.progressDialog.ProgressDialog
import id.co.ibnunaufal.template.ui.startNewActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseFragment<VM: ViewModel, B: ViewBinding, R: BaseRepository> : Fragment() {

    protected lateinit var userPreferences: UserPreference
    protected  lateinit var binding: B
    protected  lateinit var viewModel:VM
    protected val remoteDataSource = RemoteDataSource()
    public val progressDialog = ProgressDialog()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userPreferences = UserPreference(requireContext())
        binding = getFragmentBinding(inflater, container)
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())

        lifecycleScope.launch { userPreferences.authToken.first() }
        return binding.root
    }

    fun logout() = lifecycleScope.launch{
        userPreferences.clear()
        requireActivity().startNewActivity(MainActivity::class.java)
    }
    abstract fun getViewModel() : Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    abstract fun getFragmentRepository(): R

}