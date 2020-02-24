package com.hinuri.linememoproject.memo

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
    // 현재 메모장 상세 페이지에 보여지는 메모 데이터.
    private val _memoItem = MutableLiveData<Memo>()
    val memoItem : LiveData<Memo> = _memoItem

    private val _memoImageList = MutableLiveData<MutableList<String>>()
    val memoImageList : LiveData<MutableList<String>> = _memoImageList

    // 현재 메모의 상태 (보기, 작성, 편집 등)
    private val _memoState = MutableLiveData<MemoState>()
    val memoStatus : LiveData<MemoState> = _memoState

    fun setMemo(memo:Memo) {
        _memoItem.value = memo
        _memoImageList.value = memo.imageArr

        changeMemoState(MemoState.MEMO_VIEW)
    }

    // 이미지 추가
    fun addImage(imagePath: String) {
        _memoImageList.value = memoImageList.value!!.apply { add(imagePath) }
    }
    // 이미지 삭제
    fun deleteImage(imagePath:String) {
        _memoImageList.value = memoImageList.value!!.also {
            it.remove(imagePath)
        }
    }

    // 메모 작성 및 편집 후 저장!
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

        // 로컬 DB에 insert 및 update
        viewModelScope.launch {
            memoUseCase.saveMemo(
                memoItem.value!!,
                memoStatus.value!!
            )

            changeMemoState(MemoState.MEMO_EDIT_DONE)
        }
    }

    // 메모 삭제
    fun deleteMemo() {
        memoItem.value?.let {
            viewModelScope.launch {
                memoUseCase.deleteMemo(it)

                changeMemoState(MemoState.MEMO_DELETE_DONE)
            }
        }
    }

    fun changeMemoState(state: MemoState) {
        _memoState.value = state
        // 작성하는 경우는 기존에 viewModel에 있는 메모 관련 데이터 모두 초기화시킴
        if(state == MemoState.MEMO_WRITE) refreshMemo()
    }

    // 메모 내용 초기화
    private fun refreshMemo() {
        _memoItem.value = null
        _memoImageList.value = mutableListOf()
    }
}