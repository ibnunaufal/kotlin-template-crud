package id.co.ibnunaufal.template.ui.base

import androidx.lifecycle.ViewModel
import id.co.ibnunaufal.template.data.repository.BaseRepository

open class BaseViewModel(
    private val repository: BaseRepository
): ViewModel() {
}