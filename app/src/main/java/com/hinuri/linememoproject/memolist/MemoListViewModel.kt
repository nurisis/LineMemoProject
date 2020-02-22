package com.hinuri.linememoproject.memolist

import androidx.lifecycle.*
import com.hinuri.linememoproject.data.entity.Memo
import com.hinuri.linememoproject.domain.MemoUseCase

class MemoListViewModel(
    private val memoUseCase: MemoUseCase
) :ViewModel() {
//    private val _memoLists : LiveData<List<Memo>>
//    val memoLists : LiveData<List<Memo>> = _memoLists

    val memoLists: LiveData<List<Memo>> = liveData {
        emitSource(memoUseCase.getAllMemo())
    }

}