package com.hinuri.linememoproject.domain

import androidx.lifecycle.LiveData
import com.hinuri.linememoproject.data.entity.Memo
import com.hinuri.linememoproject.data.repository.MemoRepository

class MemoUseCase(
    private val memoRepository: MemoRepository
){

    suspend fun getAllMemo() : LiveData<List<Memo>> {
        return memoRepository.getAllMemo()
    }

    suspend fun insertMemo(memo:Memo) {
        memoRepository.insertMemo(memo)
    }

}