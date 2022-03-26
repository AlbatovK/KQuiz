package com.albatros.kquiz.ui.fragments.host

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

class HostViewModel(private val api: ApiService, private val repo: ClientRepo) : ViewModel() {

    private var active = true

    override fun onCleared() {
        active = false
        super.onCleared()
    }

    private val _usersInfo = MutableLiveData<List<ClientInfo>>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            while (active) {
                try { value = api.getClientsInfo(repo.sessionId) } catch (e: Exception) { }
                delay(10_000)
            }
        }
    }

    fun stopSession() {
        viewModelScope.launch(Dispatchers.Main) {
            try { api.deleteSessionIfExists(repo.sessionId) } catch (e: Exception) { }
        }
    }

    val usersInfo: LiveData<List<ClientInfo>> = _usersInfo

    private val _started = MutableLiveData<Boolean>()

    val started = _started

    fun getSessionId() = repo.sessionId

    fun getFirstQuestion() = repo.quiz.questions[0]

    fun startSession() {
        viewModelScope.launch(Dispatchers.Main) {
            _started.value = try {
                api.startSession(repo.sessionId)
                delay(500)
                true
            } catch (e: Exception) { false }
        }
    }
}