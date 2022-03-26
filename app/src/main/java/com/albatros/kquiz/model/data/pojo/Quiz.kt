package com.albatros.kquiz.model.data.pojo

import android.os.Parcelable
import com.albatros.kquiz.model.data.pojo.Question
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Quiz(

    @SerializedName("id")
    @Expose
    var id: Long = 0,

    @SerializedName("name")
    @Expose
    var name: String = "",

    @SerializedName("questions")
    @Expose
    var questions: @RawValue List<Question> = emptyList(),

    @SerializedName("topics")
    @Expose
    var topics: List<String> = emptyList()

) : Parcelable