package com.sukumar.et.ETData

import androidx.room.Room
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val ExtModule = module {
    single<ETDatabase> {
        Room.databaseBuilder(
            get(),
            ETDatabase::class.java,
            "ExpenseTracker4"
        ).build()
    }

    single<ETRepos>{
        ETRepos(get())
    }
    viewModel<ETViewModel> {
        ETViewModel(get())
    }
}