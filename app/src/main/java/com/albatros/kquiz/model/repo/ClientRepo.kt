package com.albatros.kquiz.model.repo

import com.albatros.kquiz.model.data.info.ClientInfo
import com.albatros.kquiz.model.data.pojo.Quiz

class ClientRepo {
    var quiz: Quiz = Quiz()
    var sessionId: Long = 0
    var clientInfo = ClientInfo()
    var currentPos = 0
}