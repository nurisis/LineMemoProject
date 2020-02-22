package com.hinuri.linememoproject.data.repository

import androidx.lifecycle.LiveData
import com.hinuri.linememoproject.data.entity.Memo
import com.hinuri.linememoproject.data.local.MemoLocalDataSource
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
    suspend fun getAllMemo() : LiveData<List<Memo>> {
        return withContext(ioDispatcher) {
            return@withContext memoLocalDataSource.getAllMemo()
        }
    }

    /**
     * 메모 작성 후 insert
     * */
    suspend fun insertMemo(memo:Memo) {
        return withContext(ioDispatcher) {
            return@withContext memoLocalDataSource.insertMemo(memo)
        }
    }
}