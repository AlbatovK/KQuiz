package com.albatros.kquiz.model.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseData(

    @SerializedName("code")
    @Expose
    var code: Long = 0

)