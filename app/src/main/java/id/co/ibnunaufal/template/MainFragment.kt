package id.co.ibnunaufal.template

import android.app.AlertDialog
import android.icu.text.IDNA.Info
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import id.co.ibnunaufal.template.data.InfoAdapter
import id.co.ibnunaufal.template.data.network.InfoApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainFragment: BaseFragment<MainViewModel, FragmentMainBinding, UserRepository>() {

    private val baseURL = "https://api.dev.katalis.info/main_a/"
    private val tag:String = "CHECK_RESPONSE"

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<InfoModel>

    @RequiresApi(Build.VERSION_CODES.O)
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
                }            }

        })

        newRecyclerView = view.findViewById(R.id.recyclerView)
        newRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf()

        val adapter = InfoAdapter(newArrayList)
        newRecyclerView.adapter = adapter

        getAllInfo()

        val addButton = view.findViewById<FloatingActionButton>(R.id.btnAdd)

        adapter.setOnEditClickListener(object : InfoAdapter.OnEditClickListener {
            override fun onEditClick(infoId: String) {
                val infoToEdit = newArrayList.find { it._id == infoId }
                if (infoToEdit != null) {
                    val editDataDialog = EditDataDialogFragment(infoToEdit)
                    editDataDialog.show(requireActivity().supportFragmentManager, "EditDataDialog")
//                    showEditDialog(infoToEdit)
                    editDataDialog.dismissListener = object : EditDataDialogFragment.OnDismissListener{
                        override fun onDialogDismissed() {
                            getAllInfo()
                        }

                    }
                }
            }

        })

        adapter.setOnDeleteClickListener(object : InfoAdapter.OnDeleteClickListener {
            override fun onDeleteClick(infoId: String) {
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setTitle("Delete Info")
                alertDialog.setMessage("Are you sure you want to delete this item?")

                alertDialog.setPositiveButton("Yes") { _, _ ->
                    // User confirmed deletion, proceed with deletion
                    deleteInfo(infoId)
                }

                alertDialog.setNegativeButton("No") { _, _ ->
                    // User canceled deletion, do nothing
                }

                alertDialog.show()
            }
        })

        addButton.setOnClickListener{
            val addDataDialog = AddDataDialogFragment()
            addDataDialog.show(requireActivity().supportFragmentManager, "AddDataDialog")
            addDataDialog.dismissListener = object : AddDataDialogFragment.OnDismissListener {
                override fun onDialogDismissed() {
                    getAllInfo()
                }
            }
        }
    }

    private fun deleteInfo(infoId: String){
        val api = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InfoApi::class.java)

        api.deleteInfo(infoId).enqueue(object : Callback<InfoModel> {
            override fun onResponse(call: Call<InfoModel>, response: Response<InfoModel>) {
                if (response.isSuccessful) {
                    // The info was successfully deleted. You can handle the success action here.
                    // For example, you can show a message or update your data after deletion.
                    getAllInfo() // Refresh the list after deletion
                } else {
                    // Handle the case where the deletion request was not successful.
                    Log.i(tag, "Info deletion failed")
                }
            }

            override fun onFailure(call: Call<InfoModel>, t: Throwable) {
                // Handle the network or other errors here.
                Log.i(tag, "onFailure: ${t.message}")
            }
        })

    }

    private fun getAllInfo(){
        val api = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InfoApi::class.java)

        api.getInfo().enqueue(object : Callback<List<InfoModel>>{
            override fun onResponse(
                call: Call<List<InfoModel>>,
                response: Response<List<InfoModel>>
            ) {
                val responseInfo = response.body()
                Log.i(tag, "onSuccess: $responseInfo")
                newArrayList.clear()
                responseInfo?.let {
                    newArrayList.addAll(it)
                }
                newRecyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<InfoModel>>, t: Throwable) {
                Log.i(tag, "onFailure: ${t.message}")
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