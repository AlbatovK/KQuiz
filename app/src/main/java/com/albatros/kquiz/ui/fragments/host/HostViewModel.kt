package com.albatros.kquiz.ui.fragments.host

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kquiz.model.api.ApiService
import com.albatros.kquiz.model.data.ClientInfo
import com.albatros.kquiz.model.repo.ClientRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HostViewModel(private val api: ApiService, private val repo: ClientRepo) : ViewModel() {

    private val _usersInfo = MutableLiveData<List<ClientInfo>>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            value = api.getClientsInfo(repo.sessionId)
        }
    }

    val usersInfo: LiveData<List<ClientInfo>> = _usersInfo

    private val _started = MutableLiveData<Boolean>()

    val started = _started

    fun startSession() {
        viewModelScope.launch(Dispatchers.Main) {
            _started.value = try {
                api.startSession(repo.sessionId)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    fun updateUserInfo() {
        viewModelScope.launch(Dispatchers.Main) {
            _usersInfo.value = api.getClientsInfo(repo.sessionId)
        }
    }
}