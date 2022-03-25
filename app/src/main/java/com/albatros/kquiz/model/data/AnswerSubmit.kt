package com.albatros.kquiz.model.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnswerSubmit(

    @SerializedName("id")
    @Expose
    var id: Long = 0,

    @SerializedName("position")
    @Expose
    var position: Int = 0,

    @SerializedName("right")
    @Expose
    var right: Boolean = false,

    @SerializedName("time")
    @Expose
    var time: Int = 0

) : Parcelable