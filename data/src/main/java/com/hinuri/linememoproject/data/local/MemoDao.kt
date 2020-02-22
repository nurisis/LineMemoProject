package com.hinuri.linememoproject.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.hinuri.linememoproject.data.entity.Memo

@Dao
interface MemoDao{

    @Query("SELECT * FROM memo ORDER BY updatedTime DESC")
    fun getAllMemo() : LiveData<List<Memo>>

    @Insert(onConflict = REPLACE)
    fun insertMemo(memo: Memo)

}