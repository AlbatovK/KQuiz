package com.albatros.kquiz.ui.fragments.enter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kquiz.model.api.ApiService
import com.albatros.kquiz.model.repo.ClientRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EnterViewModel(private val api: ApiService, private val repo: ClientRepo) : ViewModel() {

    private val _userId: MutableLiveData<Long?> = MutableLiveData<Long?>()

    val userId: LiveData<Long?> = _userId

    fun enterSession(id: String, name: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _userId.value = try {
                with (repo) {
                    clientInfo.id = api.enterSession(id.toLong(), name).code
                    clientInfo.name = name

                    sessionId = id.toLong()
                    quiz = api.getCurrentQuiz(repo.sessionId)

                    clientInfo.id
                }
            } catch (e: Exception) {
               null
            }
        }
    }
}