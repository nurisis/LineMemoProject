package com.hinuri.linememoproject.data.local

import androidx.lifecycle.LiveData
import com.hinuri.linememoproject.data.entity.Memo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemoLocalDataSource (
    private val memoDao: MemoDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getAllMemo() : LiveData<List<Memo>> = withContext(ioDispatcher) {
        memoDao.getAllMemo()
    }

    suspend fun insertMemo(memo:Memo) = withContext(ioDispatcher) {
        memoDao.insertMemo(memo)
    }
}