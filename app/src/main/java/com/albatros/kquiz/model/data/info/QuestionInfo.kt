package com.albatros.kquiz.model.data.info

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionInfo(

    @SerializedName("right")
    @Expose
    var right: Boolean = false,

    @SerializedName("time")
    @Expose
    var time: Int = 0

) : Parcelable