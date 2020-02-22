package com.hinuri.linememoproject.common.di

import com.hinuri.linememoproject.data.local.LineDatabase
import com.hinuri.linememoproject.data.local.MemoLocalDataSource
import com.hinuri.linememoproject.data.repository.MemoRepository
import com.hinuri.linememoproject.domain.MemoUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { MemoLocalDataSource(LineDatabase.getDatabase(androidContext()).memoDao()) }
    single { MemoRepository(get()) }
    single { MemoUseCase(get()) }
}