package com.hinuri.linememoproject.data.local

import androidx.lifecycle.LiveData
import com.hinuri.linememoproject.data.entity.Memo
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemoLocalDataSource (
    private val memoDao: MemoDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun getAllMemo() : Flowable<List<Memo>> {
        return memoDao.getAllMemo()
    }

//    suspend fun getAllMemo() : LiveData<List<Memo>> = withContext(ioDispatcher) {
//        memoDao.getAllMemo()
//    }

    fun insertMemo(memo:Memo) {
        memoDao.insertMemo(memo)
    }

//    suspend fun insertMemo(memo:Memo) = withContext(ioDispatcher) {
//        memoDao.insertMemo(memo)
//    }

    fun updateMemo(memo:Memo) {
        memoDao.updateMemo(memo)
    }

//    suspend fun updateMemo(memo:Memo) = withContext(ioDispatcher) {
//        memoDao.updateMemo(memo)
//    }

    suspend fun deleteMemo(memo:Memo) = withContext(ioDispatcher) {
        memoDao.deleteMemo(memo)
    }
}