package com.albatros.kquiz.ui.fragments.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kquiz.model.api.ApiService
import com.albatros.kquiz.model.data.AnswerSubmit
import com.albatros.kquiz.model.data.Question
import com.albatros.kquiz.model.repo.ClientRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel(
    private val question: Question,
    private val api: ApiService,
    private val repo: ClientRepo
) : ViewModel() {

    private val _onActionEnd = MutableLiveData<Boolean>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            delay(30_000)
            value = false
        }
    }

    private val _counter = MutableLiveData<Int>().apply {
        value = 200
        viewModelScope.launch(Dispatchers.Main) {
            while (value!! > 0) {
                delay(150)
                value = value!! - 1
            }
        }
    }

    val counter: LiveData<Int> = _counter


    val onActionEnd: LiveData<Boolean> = _onActionEnd

    private val _onSuccessSubmit = MutableLiveData<Boolean>()

    val onSuccessSubmit: LiveData<Boolean> = _onSuccessSubmit

    fun submitData(answer: String) {
        val data = AnswerSubmit(repo.clientInfo.id, repo.currentPos, answer == question.answer, 0)
        repo.currentPos++
        viewModelScope.launch(Dispatchers.Main) {
            try {
                api.sendAnswerSubmitData(data, repo.sessionId)
            } catch (e: Exception) {}
        }
    }

}