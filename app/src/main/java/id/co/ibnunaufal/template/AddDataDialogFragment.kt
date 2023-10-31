package id.co.ibnunaufal.template

import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import com.google.android.material.button.MaterialButton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.fragment.app.DialogFragment
import id.co.ibnunaufal.template.data.model.InfoModel
import id.co.ibnunaufal.template.data.model.PostInfoModel
import id.co.ibnunaufal.template.data.network.InfoApi
import id.co.ibnunaufal.template.ui.base.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddDataDialogFragment : DialogFragment() {

    private val BASE_URL = "https://api.dev.katalis.info/main_a/"
    private val TAG:String = "CHECK_RESPONSE"

    private lateinit var editTextId: EditText
    private lateinit var editTextVersionCode: EditText
    private lateinit var radioGroupIsUrgent: RadioGroup
    private lateinit var radioGroupStatusMigrasi: RadioGroup
    private lateinit var editTextMsg: EditText

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog, container, false)

        editTextId = view.findViewById(R.id.editTextId)
        editTextVersionCode = view.findViewById(R.id.editTextVersionCode)
        radioGroupIsUrgent = view.findViewById(R.id.radioGroupIsUrgent)
        radioGroupStatusMigrasi = view.findViewById(R.id.radioGroupStatusMigrasi)
        editTextMsg = view.findViewById(R.id.editTextMsg)

        val addButton = view.findViewById<Button>(R.id.postButton)

        addButton.setOnClickListener {
            // Get data from EditText fields
            val id = editTextId.text.toString()
            val versionCode = editTextVersionCode.text.toString()
            val msg = editTextMsg.text.toString()

            val urgent = when (radioGroupIsUrgent.checkedRadioButtonId) {
                R.id.radioButtonUrgentTrue -> true
                R.id.radioButtonUrgentFalse -> false
                else -> false
            }

            val migrate = when (radioGroupStatusMigrasi.checkedRadioButtonId) {
                R.id.radioButtonStatusMigrasiTrue -> true
                R.id.radioButtonStatusMigrasiFalse -> false
                else -> false
            }

            // Get the current date and time
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val date = currentDateTime.format(formatter)

            // Create a PostInfoModel with the collected data
            val info = PostInfoModel(id, versionCode, urgent, migrate, listOf(msg), date)

            // Call a function to post the data to the server
            postDataToServer(info)

            // Close the dialog
        }

        return view
    }

    interface OnDismissListener {
        fun onDialogDismissed()
    }

    var dismissListener: OnDismissListener? = null

    // ...

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onDialogDismissed()
    }

//    override fun onDismiss(dialog: DialogInterface) {
//        super.onDismiss(dialog)
//        Log.i("ondismiss", "dismiss called fff")
////        val targetFragment = targetFragment as? BaseFragment<*, *, *>
////        targetFragment?.onDialogDismissed()
//    }

    fun postDataToServer(info: PostInfoModel) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val infoApi = retrofit.create(InfoApi::class.java)

        val call = infoApi.postInfo(info)

        call.enqueue(object : Callback<InfoModel> {
            override fun onResponse(call: Call<InfoModel>, response: Response<InfoModel>) {
                if (response.isSuccessful) {
                    // Data was successfully posted
                    val infoModel = response.body()
                    if (infoModel != null) {
                        Log.i(TAG, "Data successfuly added $infoModel")
                    }
                    dismiss()
                } else {
                    // Handle the error response
                    Log.e(TAG, "Post data failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<InfoModel>, t: Throwable) {
                // Handle the network request failure
                Log.e(TAG, "Post data failed: ${t.message}")
            }
        })
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }
}