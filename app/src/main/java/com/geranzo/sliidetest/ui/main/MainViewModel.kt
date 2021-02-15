package com.geranzo.sliidetest.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.geranzo.domain.entity.User
import com.geranzo.domain.repository.Result
import com.geranzo.domain.usecase.ObserveUsersInLocalDbUseCase
import com.geranzo.domain.usecase.RefreshUsersInLocalDbWithRemoteOnesUseCase
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor(
    usersInDbUseCase: ObserveUsersInLocalDbUseCase,
    private val refreshFromBackendUseCase: RefreshUsersInLocalDbWithRemoteOnesUseCase
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users = _users as LiveData<List<User>>

    private val _error = MutableLiveData<String?>(null)
    val error = _error as LiveData<String?>

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing = _isRefreshing as LiveData<Boolean>

    private val disposableUsers: Disposable = usersInDbUseCase.get().subscribe { listUsers ->
        _users.postValue(listUsers)
    }

    private var disposableBackend: Disposable? = null

    init {
        refresh()
    }


    fun refresh() {
        disposableBackend?.dispose()
        disposableBackend = refreshFromBackendUseCase.refresh()
            .subscribe { result ->
                // loading
                _isRefreshing.postValue(result is Result.Loading)
                // or error
                (result as? Result.Error)?.let { setError(result.throwable.message) }
            }
    }

    fun clearError() {
        _error.postValue(null)
    }

    private fun setError(message: String?) {
        _error.postValue(message)
    }

    override fun onCleared() {
        super.onCleared()
        disposableUsers.dispose()
        disposableBackend?.dispose()
    }


    class Factory @Inject constructor(private val vm: MainViewModel) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = vm as T
    }
}
