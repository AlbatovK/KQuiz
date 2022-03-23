package com.albatros.kquiz.model.module

import com.albatros.kquiz.ui.fragments.client.ClientViewModel
import com.albatros.kquiz.ui.fragments.enter.EnterViewModel
import com.albatros.kquiz.ui.fragments.host.HostViewModel
import com.albatros.kquiz.ui.fragments.list.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { EnterViewModel(get(), get()) }
    viewModel { ListViewModel(get(), get()) }
    viewModel { HostViewModel(get(), get()) }
    viewModel { ClientViewModel(get(), get()) }
}