package com.hinuri.linememoproject.memo

import com.hinuri.linememoproject.common.BasePresenter
import com.hinuri.linememoproject.common.BaseView
import com.hinuri.linememoproject.data.entity.Memo

/**
 * This specifies the contract between the view and the presenter.
 */
interface WriteMemoContract {
    interface View : BaseView<Presenter> {
        fun setTitle(title: String?)
        fun setDescription(description: String?)
    }

    interface Presenter: BasePresenter {
        fun saveMemo(memo: Memo)

        fun addImage(imagePath: String)
        fun deleteImage(imagePath:String)

        fun loadMemo()
    }
}