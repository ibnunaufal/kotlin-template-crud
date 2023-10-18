package id.co.ibnunaufal.template

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.co.ibnunaufal.template.data.model.InfoModel
import id.co.ibnunaufal.template.data.network.Resource
import id.co.ibnunaufal.template.data.network.UserApi
import id.co.ibnunaufal.template.data.repository.UserRepository
import id.co.ibnunaufal.template.databinding.FragmentMainBinding
import id.co.ibnunaufal.template.ui.base.BaseFragment
import id.co.ibnunaufal.template.ui.handleApiError
import kotlinx.coroutines.runBlocking

import androidx.appcompat.app.AppCompatActivity
import id.co.ibnunaufal.template.data.InfoAdapter
import id.co.ibnunaufal.template.data.network.InfoApi
import kotlinx.android.synthetic.clearFindViewByIdCache
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainFragment: BaseFragment<MainViewModel, FragmentMainBinding, UserRepository>() {

    private val BASE_URL = "https://api.dev.katalis.info/main_a/"
    private val TAG:String = "CHECK_RESPONSE"

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<InfoModel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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

        newRecyclerView = view.findViewById(R.id.recyclerView)
        newRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf()

        val adapter = InfoAdapter(newArrayList)
        newRecyclerView.adapter = adapter

        getAllInfo()
    }

    private fun getAllInfo(){
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InfoApi::class.java)

        api.getInfo().enqueue(object : Callback<InfoModel>{
            override fun onResponse(
                call: Call<InfoModel>,
                response: Response<InfoModel>
            ) {
                val responseInfo = response.body()
                Log.i(TAG, "onSucces: $responseInfo")
                newArrayList.clear()
                responseInfo?.let {
                    newArrayList.addAll(listOf(it))
                }
                newRecyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<InfoModel>, t: Throwable) {
                Log.i(TAG, "onFailure: ${t.message}")
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