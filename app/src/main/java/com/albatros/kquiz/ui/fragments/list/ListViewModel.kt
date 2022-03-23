package com.albatros.kquiz.ui.fragments.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kquiz.model.api.ApiService
import com.albatros.kquiz.model.data.Quiz
import com.albatros.kquiz.model.repo.ClientRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel(private val api: ApiService, private val repo: ClientRepo) : ViewModel() {

    private val _quizzes = MutableLiveData<List<Quiz>?>().apply {
        viewModelScope.launch {
            value = try {
                api.getQuizzes()
            } catch (e: Exception) {
                null
            }
        }
    }

    val quizzes: LiveData<List<Quiz>?> = _quizzes

    private val _sessionId: MutableLiveData<Long?> = MutableLiveData()

    val sessionId: LiveData<Long?> = _sessionId

    private val _userId: MutableLiveData<Long?> = MutableLiveData()

    val userId: LiveData<Long?> = _userId

    fun registerSession(quiz: Quiz) {
        repo.quiz = quiz
        viewModelScope.launch(Dispatchers.Main) {
            _sessionId.value = try {
                repo.sessionId = api.registerSession(quiz.id).code
                repo.sessionId
            } catch (e: Exception) {
                null
            }
        }
    }

    fun enterSession(id: Long, name: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _userId.value = try {
                val clientId = api.enterSession(id, name).code
                repo.clientInfo.apply {
                    this.id = clientId
                    this.name = name
                }.id
            } catch (e: Exception) {
                null
            }
        }
    }
}