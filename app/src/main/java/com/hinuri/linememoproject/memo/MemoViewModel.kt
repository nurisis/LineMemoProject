package com.hinuri.linememoproject.memo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hinuri.linememoproject.data.entity.Memo
import com.hinuri.linememoproject.domain.MemoUseCase
import com.hinuri.linememoproject.entity.MemoState
import kotlinx.coroutines.launch

class MemoViewModel(
    private val memoUseCase: MemoUseCase
) :ViewModel(){
    private val _memoItem = MutableLiveData<Memo>().apply { Memo() }
    val memoItem : LiveData<Memo> = _memoItem

    private val _memoImageList = MutableLiveData<MutableList<String>>()
    val memoImageList : LiveData<MutableList<String>> = _memoImageList

    private val _memoState = MutableLiveData<MemoState>()
    val memoStatus : LiveData<MemoState> = _memoState

    fun setMemo(memo:Memo) {
        _memoItem.value = memo
        _memoImageList.value = memo.imageArr

        changeMemoState(MemoState.MEMO_VIEW)
    }

    fun addImage(imagePath: String) {
        Log.d("LOG>>","addImage >> ")

        _memoImageList.value = memoImageList.value!!.apply { add(imagePath) }

        Log.d("LOG>>","이미지 추가 후 리스트 : ${memoImageList.value}")
    }

    fun deleteImage(imagePath:String) {
        _memoImageList.value = memoImageList.value!!.also {
            it.remove(imagePath)
        }
    }

    fun saveMemo(title:String, content:String) {
        // 새로 작성하는 것이므로 생성
        if(memoItem.value == null) {
            _memoItem.value = Memo(title = title, content = content).apply {
                memoImageList.value?.let { imageArr = it }
            }
        }
        // 기존 내용에 변경 사항 업데이트
        else {
            memoItem.value!!.apply {
                this.title = title
                this.content = content
                memoImageList.value?.let { this.imageArr = it }
            }
        }

        viewModelScope.launch {
            memoUseCase.saveMemo(
                memoItem.value!!,
                memoStatus.value!!
            )

            changeMemoState(MemoState.MEMO_EDIT_DONE)
        }
    }

    fun changeMemoState(state: MemoState) {
        _memoState.value = state
        if(state == MemoState.MEMO_WRITE) refreshMemo()
    }

    fun deleteMemo() {
        memoItem.value?.let {
            viewModelScope.launch {
                memoUseCase.deleteMemo(it)

                changeMemoState(MemoState.MEMO_DELETE_DONE)
            }
        }
    }

    // 메모 내용 초기화
    private fun refreshMemo() {
        _memoItem.value = null
        _memoImageList.value = mutableListOf()
    }
}