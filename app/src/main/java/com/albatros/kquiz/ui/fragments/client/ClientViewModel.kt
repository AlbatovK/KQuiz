package com.albatros.kquiz.ui.fragments.client

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

class ClientViewModel(private val api: ApiService, private val repo: ClientRepo) : ViewModel() {

    private val _usersInfo = MutableLiveData<List<ClientInfo>>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            while (active) {
                try { value = api.getClientsInfo(repo.sessionId) } catch (e: Exception) { }
                delay(10_000)
            }
        }
    }


    fun getFirstQuestion() = repo.quiz.questions[0]

    private var active = true

    private val _started = MutableLiveData<Boolean>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            while (active) {
                try {
                    value = api.hasStarted(repo.sessionId)
                } catch (e: Exception) {}
                delay(1_000)
            }
        }
    }

    override fun onCleared() {
        active = false
        super.onCleared()
    }

    val started: LiveData<Boolean> = _started

    val usersInfo: LiveData<List<ClientInfo>> = _usersInfo

}