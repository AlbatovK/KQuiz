package com.albatros.kquiz.ui.fragments.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kquiz.model.api.ApiService
import com.albatros.kquiz.model.data.info.ClientInfo
import com.albatros.kquiz.model.data.pojo.Question
import com.albatros.kquiz.model.repo.ClientRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ResultViewModel(
    private val question: Question,
    private val api: ApiService,
    private val repo: ClientRepo
) : ViewModel() {

    private val _onPassiveStateEnded = MutableLiveData<Boolean>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            delay(5_000)
            value = true
        }
    }

    fun stopSession() {
        repo.currentPos = 0
        viewModelScope.launch(Dispatchers.Main) {
            try { api.deleteSessionIfExists(repo.sessionId) } catch (e: Exception) { }
        }
    }

    private val _usersInfo = MutableLiveData<List<ClientInfo>>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            value = api.getClientsInfo(repo.sessionId).sortedByDescending(ClientInfo::score)
        }
    }

    val usersInfo: LiveData<List<ClientInfo>> = _usersInfo


    val onPassiveStateEnded: LiveData<Boolean> = _onPassiveStateEnded

    fun getNextQuestion() = repo.quiz.questions.getOrNull(repo.currentPos)

}