package com.hinuri.linememoproject.memolist

import androidx.lifecycle.*
import com.hinuri.linememoproject.data.entity.Memo
import com.hinuri.linememoproject.domain.MemoUseCase

class MemoListViewModel(
    private val memoUseCase: MemoUseCase
) :ViewModel() {

    val memoLists: LiveData<List<Memo>> = liveData {
        emitSource(memoUseCase.getAllMemo())
    }

    private val _memoClicked = MutableLiveData<Memo>()
    val memoClicked : LiveData<Memo> = _memoClicked

    fun clickMemoItem(memo:Memo) {
        _memoClicked.value = memo
    }

}