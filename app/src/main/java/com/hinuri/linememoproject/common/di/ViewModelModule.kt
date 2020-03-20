package com.hinuri.linememoproject.common.di

import com.hinuri.linememoproject.addmedia.AddMediaViewModel
import com.hinuri.linememoproject.memo.MemoViewModel
import com.hinuri.linememoproject.memolist.MemoListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MemoListViewModel(get()) }
    viewModel { MemoViewModel(get()) }
    viewModel { AddMediaViewModel(androidApplication()) }
}