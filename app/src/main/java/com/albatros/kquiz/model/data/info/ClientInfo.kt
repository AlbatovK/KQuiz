package com.albatros.kquiz.model.data.info

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ClientInfo(

    @SerializedName("id")
    @Expose
    var id: Long = 0,

    @SerializedName("name")
    @Expose
    var name: String = "",

    @SerializedName("started")
    @Expose
    var started: Boolean = false,

    @SerializedName("questionMap")
    @Expose
    var questionMap: HashMap<Int, QuestionInfo> = HashMap()

)