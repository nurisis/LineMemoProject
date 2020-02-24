package com.hinuri.linememoproject.domain

/**
 * 각종 메모 관련 데이터 처리
 * */

import androidx.lifecycle.LiveData
import com.hinuri.linememoproject.data.entity.Memo
import com.hinuri.linememoproject.data.repository.MemoRepository
import com.hinuri.linememoproject.entity.MemoState
import java.text.SimpleDateFormat
import java.util.*

class MemoUseCase(
    private val memoRepository: MemoRepository
){

    suspend fun getAllMemo() : LiveData<List<Memo>> {
        return memoRepository.getAllMemo()
    }

    suspend fun saveMemo(memo:Memo, memoState: MemoState) {
        val updateTime = SimpleDateFormat("YYYY_MM_dd_HH:mm:ss").format(Date())

        when(memoState) {
            MemoState.MEMO_WRITE ->  {
                memoRepository.insertMemo(memo.apply {
                    createdTime = updateTime
                    updatedTime = updateTime
                })
            }
            // 편집 후 저장하는 것이므로, updatedTime만 업데이트 해줌.
            MemoState.MEMO_EDIT -> {
                memoRepository.updateMemo(memo.apply { updatedTime = updateTime })
            }
        }
    }

    suspend fun deleteMemo(memo:Memo) {
        memoRepository.deleteMemo(memo)
    }

}