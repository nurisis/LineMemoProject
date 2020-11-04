package com.hinuri.linememoproject.memo

import com.hinuri.linememoproject.common.util.BaseSchedulerProvider
import com.hinuri.linememoproject.data.entity.Memo
import com.hinuri.linememoproject.domain.MemoUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable

class WriteMemoPresenter(
    private val memoUseCase: MemoUseCase,
    private val memoView: WriteMemoContract.View,
    private val schedulerProvider: BaseSchedulerProvider
) : WriteMemoContract.Presenter {

    private val memoId: Int? = null
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        memoView.setPresenter(this)
    }

    override fun saveMemo(memo: Memo) {
        compositeDisposable.clear()

        // TODO :: saveMemo와 같이 저장만하는애들은 Rx를 어떻게 써야...?

        val disposable = memoUseCase
            .saveMemo(memo, isNewTask())
//            .subscribeOn(schedulerProvider.io())
//            .observeOn(schedulerProvider.ui())
//            .subscribe {
//                memoView.showMemoList(it)
//            }
//        compositeDisposable.add(disposable)
    }

    override fun addImage(imagePath: String) {
        TODO("Not yet implemented")
    }

    override fun deleteImage(imagePath: String) {
        TODO("Not yet implemented")
    }

    override fun loadMemo() {
        TODO("Not yet implemented")
    }

    private fun isNewTask(): Boolean {
        return memoId == null
    }

    override fun subscribe() {
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}