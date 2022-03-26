package com.albatros.kquiz.model.data.pojo

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Question(

    @SerializedName("description")
    @Expose
    var description: String = "",

    @SerializedName("variants")
    @Expose
    var variants: @RawValue List<String> = emptyList(),

    @SerializedName("answer")
    @Expose
    var answer: String = ""

) : Parcelable