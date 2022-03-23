package com.albatros.kquiz.model.module

import com.albatros.kquiz.model.repo.ClientRepo
import org.koin.dsl.module

val repoModule = module {
    single { ClientRepo() }
}