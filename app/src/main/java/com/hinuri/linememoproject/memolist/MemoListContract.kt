package com.hinuri.linememoproject.memolist

import androidx.annotation.NonNull
import com.hinuri.linememoproject.common.BasePresenter
import com.hinuri.linememoproject.common.BaseView
import com.hinuri.linememoproject.data.entity.Memo

/**
 * This specifies the contract between the view and the presenter.
 */
interface MemoListContract {
    interface View : BaseView<Presenter> {
        fun showMemoDetail(memo: Memo)

        fun showMemoList(list: List<Memo>)
    }

    interface Presenter: BasePresenter {
        fun clickMemo(@NonNull memo: Memo)

        fun loadMemoList()
    }
}