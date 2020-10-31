package com.hinuri.linememoproject.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.hinuri.linememoproject.data.entity.Memo
import io.reactivex.rxjava3.core.Flowable

@Dao
interface MemoDao{

    @Query("SELECT * FROM memo ORDER BY updatedTime DESC")
    fun getAllMemo() : Flowable<List<Memo>>
//    @Query("SELECT * FROM memo ORDER BY updatedTime DESC")
//    fun getAllMemo() : LiveData<List<Memo>>

    @Insert(onConflict = REPLACE)
    fun insertMemo(memo: Memo)

    @Delete
    fun deleteMemo(memo:Memo)

    @Update
    fun updateMemo(memo: Memo)

}