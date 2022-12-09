package id.co.ibnunaufal.template.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import id.co.ibnunaufal.template.MainActivity
import id.co.ibnunaufal.template.MainFragment
import id.co.ibnunaufal.template.data.network.Resource
import id.co.ibnunaufal.template.ui.base.BaseFragment


fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}
fun <A : Activity> Activity.startNewActivityBundle(bundle: Bundle, activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}


fun Fragment.handleApiError(
    failure: Resource.Failure,
    retry: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> {
            if (this is MainFragment) {
                requireActivity().startNewActivity(MainActivity::class.java)
            }
        }

        failure.errorCode == 401 -> {
            if (this is MainFragment) {
                requireView().snackbar("Username atau password tidak sesuai")
                progressDialog.dialog.dismiss()

            } else {
                (this as BaseFragment<*, *, *>).logout()
            }
        }
        failure.errorCode == 500 -> {
            if(this is MainFragment){
                requireView().snackbar("Sistem sedang diperbaharui~")
            }
        }
        failure.errorCode == 400 -> {

        }
        failure.errorCode == 404 -> {

        }
        else -> {
            if(this is MainFragment){
                progressDialog.dialog.dismiss()
            }
            val error = failure.errorBody
            if (error != null) {
                requireView().snackbar(error.toString())
            }
        }
    }
}