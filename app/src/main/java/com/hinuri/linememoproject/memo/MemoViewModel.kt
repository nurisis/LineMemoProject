package com.hinuri.linememoproject.memo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hinuri.linememoproject.data.entity.Memo
import com.hinuri.linememoproject.domain.MemoUseCase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MemoViewModel(
    private val memoUseCase: MemoUseCase
) :ViewModel(){
    val memoTitle = MutableLiveData<String>()

    val memoContents = MutableLiveData<String>()

    private val _memoImageList = MutableLiveData<MutableList<String>>()
    val memoImageList : LiveData<MutableList<String>> = _memoImageList

    fun writeMemoComplete() {

    }

    fun saveMemo() {
        viewModelScope.launch {
            memoUseCase.insertMemo(Memo(
                title = memoTitle.value ?: "",
                content = memoContents.value ?: "",
                imageArr = memoImageList.value ?: mutableListOf(),
                createdTime = SimpleDateFormat("YYYY_MM_dd_HH:mm:ss").format(Date()),
                updatedTime = SimpleDateFormat("YYYY_MM_dd_HH:mm:ss").format(Date())
            ))
        }
    }
}