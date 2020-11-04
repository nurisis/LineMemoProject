package com.hinuri.linememoproject.memo

import com.hinuri.linememoproject.common.BasePresenter
import com.hinuri.linememoproject.common.BaseView
import com.hinuri.linememoproject.data.entity.Memo

/**
 * This specifies the contract between the view and the presenter.
 */
interface MemoDetailContract {
    interface View : BaseView<Presenter> {
        fun showMemo(memo: Memo)

        fun refreshMemo(memo: Memo)
    }

    interface Presenter: BasePresenter {
        fun deleteMemo()

        fun loadMemo()
    }
}