package com.albatros.kquiz.ui.fragments.creator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kquiz.model.api.ApiService
import com.albatros.kquiz.model.data.pojo.Question
import com.albatros.kquiz.model.data.pojo.Quiz
import kotlinx.coroutines.launch
import java.util.*

class CreatorViewModel(private val api: ApiService) : ViewModel() {

    private val _topics: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf("Добавить"))
    val topics: LiveData<MutableList<String>> = _topics

    private val _questions: MutableLiveData<MutableList<Question>> = MutableLiveData(mutableListOf())
    val questions: LiveData<MutableList<Question>> = _questions

    fun areTopicsSelected() = topics.value?.size ?: 0 > 1

    fun deleteTopic(pos: Int) {
        _topics.value?.removeAt(pos)
        _topics.value = _topics.value?.toMutableList()
    }

    fun enable() {
        viewModelScope.launch {
            try { api.enableService() } catch (e: Exception) {}
        }
    }

    private val _inputValid: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val inputValid: LiveData<Boolean> = _inputValid

    fun processQuiz(name: String) {
        val valid = name.isNotEmpty() && (_topics.value?.size ?: 0) > 1 && _questions.value?.isNotEmpty() ?: false
        _inputValid.value = if (valid) {
            try {
                val tpcs = _topics.value!!.subList(1, _topics.value!!.size)
                val q = Quiz(UUID.randomUUID().mostSignificantBits, name, _questions.value!!, tpcs)
                viewModelScope.launch { api.createQuiz(q) }
                true
            } catch (e: Exception) {
                false
            }
        } else false
    }

    fun addTopic(topic: String) {
        if (topic.isBlank())
            return
        _topics.value?.add(topic)
        _topics.value = _topics.value?.toMutableList()
    }

    private val _qInputValid: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val qInputValid: LiveData<Boolean> = _qInputValid

    fun addQuestion(description: String, variants: List<String>, answer: String) {
        val rVars = variants.filter { it.isNotEmpty() && it.isNotBlank() }
        val valid =
            description.isNotEmpty() && description.isNotBlank()
                    && rVars.isNotEmpty() && answer.isNotBlank() && answer.isNotEmpty()
        _qInputValid.value = if (valid) {
            _questions.value?.add(Question(description, rVars, answer))
            _questions.value = _questions.value?.toMutableList()
            true
        } else false
    }

}