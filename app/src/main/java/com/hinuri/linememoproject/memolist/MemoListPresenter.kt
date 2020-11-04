package com.hinuri.linememoproject.memolist

import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import com.hinuri.linememoproject.common.util.BaseSchedulerProvider
import com.hinuri.linememoproject.data.entity.Memo
import com.hinuri.linememoproject.domain.MemoUseCase
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.BiConsumer
import io.reactivex.rxjava3.internal.util.ConnectConsumer
import java.util.function.Consumer

class MemoListPresenter(
    private val memoUseCase: MemoUseCase,
    private val memoView: MemoListContract.View,
    private val schedulerProvider: BaseSchedulerProvider
) : MemoListContract.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        memoView.setPresenter(this)
    }

    override fun clickMemo(memo: Memo) {
    }

    override fun loadMemoList() {
        compositeDisposable.clear()

        val disposable = memoUseCase
            .getAllMemo()
//            .toList()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe {
                memoView.showMemoList(it)
            }
        compositeDisposable.add(disposable)
    }

    override fun subscribe() {
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}