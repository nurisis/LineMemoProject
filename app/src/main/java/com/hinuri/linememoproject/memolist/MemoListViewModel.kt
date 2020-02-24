package com.hinuri.linememoproject.memolist

import androidx.lifecycle.*
import com.hinuri.linememoproject.data.entity.Memo
import com.hinuri.linememoproject.domain.MemoUseCase

class MemoListViewModel(
    private val memoUseCase: MemoUseCase
) :ViewModel() {

    // 로컬 DB에서 메모리스트를 LiveData로 불러옴
    val memoLists: LiveData<List<Memo>> = liveData {
        emitSource(memoUseCase.getAllMemo())
    }

    // 리스트에서 클릭한 메모 아이템을 담음
    private val _memoClicked = MutableLiveData<Memo>()
    val memoClicked : LiveData<Memo> = _memoClicked

    fun clickMemoItem(memo:Memo) {
        _memoClicked.value = memo
    }

}