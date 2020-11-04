package com.hinuri.linememoproject.data.repository

import androidx.lifecycle.LiveData
import com.hinuri.linememoproject.data.entity.Memo
import com.hinuri.linememoproject.data.local.MemoLocalDataSource
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemoRepository(
    // 추후에 remote api 붙일 때 여기에 memoRemoteDataSource 연결하면 될듯
    private val memoLocalDataSource: MemoLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    /**
     * 모든 메모 리스트 호출
     * */
    fun getAllMemo() : Flowable<List<Memo>> {
        return memoLocalDataSource.getAllMemo()
    }
//    suspend fun getAllMemo() : LiveData<List<Memo>> {
//        return withContext(ioDispatcher) {
//            memoLocalDataSource.getAllMemo()
//        }
//    }

    /**
     * 메모 작성 후 insert
     * */
    fun insertMemo(memo:Memo) {
        return memoLocalDataSource.insertMemo(memo)
    }
//    suspend fun insertMemo(memo:Memo) {
//        return withContext(ioDispatcher) {
//            memoLocalDataSource.insertMemo(memo)
//        }
//    }

    /**
     * 메모 작성 후 update
     * */
    fun updateMemo(memo:Memo) {
        memoLocalDataSource.updateMemo(memo)
    }
//    suspend fun updateMemo(memo:Memo) {
//        return withContext(ioDispatcher) {
//            memoLocalDataSource.updateMemo(memo)
//        }
//    }

    /**
     * 메모 삭제
     * */
    suspend fun deleteMemo(memo:Memo) {
        return withContext(ioDispatcher) {
            memoLocalDataSource.deleteMemo(memo)
        }
    }
}