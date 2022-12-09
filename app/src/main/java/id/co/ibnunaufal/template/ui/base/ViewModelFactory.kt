package id.co.ibnunaufal.template.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.co.ibnunaufal.template.MainViewModel
import id.co.ibnunaufal.template.data.repository.BaseRepository
import id.co.ibnunaufal.template.data.repository.UserRepository
import java.lang.IllegalArgumentException

class ViewModelFactory(private val repository: BaseRepository): ViewModelProvider.NewInstanceFactory()
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repository as UserRepository) as T

            else -> throw IllegalArgumentException("ViewModelClass not found")
        }
    }
}