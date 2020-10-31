//package com.hinuri.linememoproject.memolist
//
//import androidx.annotation.NonNull
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.liveData
//import com.hinuri.linememoproject.common.util.BaseSchedulerProvider
//import com.hinuri.linememoproject.data.entity.Memo
//import com.hinuri.linememoproject.domain.MemoUseCase
//import io.reactivex.rxjava3.disposables.CompositeDisposable
//
//class MemoListViewModel(
//    private val memoUseCase: MemoUseCase
//) :ViewModel() {
//
//    @NonNull
//    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
//
//    // 로컬 DB에서 메모리스트를 LiveData로 불러옴
//    val memoLists: LiveData<List<Memo>> = liveData {
//        emitSource(memoUseCase.getAllMemo())
//    }
//
//    // 리스트에서 클릭한 메모 아이템을 담음
//    private val _memoClicked = MutableLiveData<Memo>()
//    val memoClicked : LiveData<Memo> = _memoClicked
//
//    fun clickMemoItem(memo:Memo) {
//        _memoClicked.value = memo
//    }
//
//}