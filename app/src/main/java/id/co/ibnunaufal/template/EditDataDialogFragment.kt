package id.co.ibnunaufal.template

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
import androidx.fragment.app.DialogFragment
import id.co.ibnunaufal.template.data.model.InfoModel
import id.co.ibnunaufal.template.data.network.InfoApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditDataDialogFragment(infoModel: InfoModel) : DialogFragment() {
    private val baseURL = "https://api.dev.katalis.info/main_a/"
    private val tag:String = "CHECK_RESPONSE"

    private lateinit var editTitle: EditText
    private lateinit var editVersion: EditText
    private lateinit var radioGroupIsUrgent: RadioGroup
    private lateinit var radioGroupStatusMigrasi: RadioGroup
    private lateinit var editMessage: EditText
    private var infoModel: InfoModel

    init {
        this.infoModel = infoModel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.edit_dialog, container, false)

        //Initialize
        editTitle = view.findViewById(R.id.editTextId)
        editVersion = view.findViewById(R.id.editTextVersionCode)
        editMessage = view.findViewById(R.id.editTextMsg)
        radioGroupIsUrgent = view.findViewById(R.id.radioGroupIsUrgentEdit)
        radioGroupStatusMigrasi = view.findViewById(R.id.radioGroupStatusMigrasiEdit)

        //Set the default value
        editTitle.setText(infoModel.id)
        editVersion.setText(infoModel.versionCode)
        if (infoModel.isUrgent) {
            radioGroupIsUrgent.check(R.id.radioButtonUrgentTrue)
        } else {
            radioGroupIsUrgent.check(R.id.radioButtonUrgentFalse)
        }
        if (infoModel.statusMigrasi) {
            radioGroupStatusMigrasi.check(R.id.radioButtonStatusMigrasiTrue)
        } else {
            radioGroupStatusMigrasi.check(R.id.radioButtonStatusMigrasiFalse)
        }
        editMessage.setText(infoModel.msg.toString())

        val editButton = view.findViewById<Button>(R.id.editButton)

        editButton.setOnClickListener {
            val updatedTitle = editTitle.text.toString()
            val updatedVersion = editVersion.text.toString()
            val updatedMessage = editMessage.text.toString()

            val updatedUrgent = when (radioGroupIsUrgent.checkedRadioButtonId) {
                R.id.radioButtonUrgentTrue -> true
                R.id.radioButtonUrgentFalse -> false
                else -> false
            }

            val updatedMigrate = when (radioGroupStatusMigrasi.checkedRadioButtonId) {
                R.id.radioButtonStatusMigrasiTrue -> true
                R.id.radioButtonStatusMigrasiFalse -> false
                else -> false
            }

            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val date = currentDateTime.format(formatter)

            val updatedInfo = InfoModel(infoModel._id, updatedTitle, updatedVersion,
                updatedUrgent, updatedMigrate, listOf(updatedMessage), date
            )
            editInfo(infoModel._id, updatedInfo)
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

    private fun editInfo(infoId: String, updatedInfo: InfoModel) {
        val api = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InfoApi::class.java)

        api.editInfo(infoId, updatedInfo).enqueue(object : Callback<InfoModel> {
            override fun onResponse(call: Call<InfoModel>, response: Response<InfoModel>) {
                if (response.isSuccessful) {
                    // The info was successfully edited. You can handle the success action here.
                    // For example, you can show a message or update your data after editing.
                    // Refresh the list after editing
                    Log.i("Response Message: ", response.message())
                    Log.i("Response Code: ", response.code().toString())
                    Log.i("Response Body", response.body().toString())
                    dismiss()
                } else {
                    // Handle the case where the edit request was not successful.
                    Log.i(tag, "Info edit failed")
                    Log.i(tag, "Response Code: ${response.code()}")
                    Log.i(tag, "Response Body: ${response.body()}")
                    Log.i(tag, "Response Error Body: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<InfoModel>, t: Throwable) {
                // Handle the network or other errors here.
                Log.i(tag, "onFailure: ${t.message}")
            }
        })
    }


}