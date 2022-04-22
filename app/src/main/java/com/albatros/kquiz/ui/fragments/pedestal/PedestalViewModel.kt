package com.albatros.kquiz.ui.fragments.pedestal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kquiz.model.api.ApiService
import com.albatros.kquiz.model.data.info.ClientInfo
import com.albatros.kquiz.model.repo.ClientRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PedestalViewModel(private val api: ApiService, private val repo: ClientRepo) : ViewModel() {

    private val _info = MutableLiveData<List<ClientInfo>>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            value = api.getClientsInfo(repo.sessionId).sortedByDescending(ClientInfo::score)
        }
    }

    private val _onPassiveStateEnded = MutableLiveData<Boolean>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            delay(15_000)
            value = true
        }
    }

    val onPassiveStateEnded: LiveData<Boolean> = _onPassiveStateEnded

    val info: LiveData<List<ClientInfo>> = _info

    fun stopSession() {
        repo.currentPos = 0
        viewModelScope.launch(Dispatchers.Main) {
            try { api.deleteSessionIfExists(repo.sessionId) } catch (e: Exception) { }
        }
    }
}