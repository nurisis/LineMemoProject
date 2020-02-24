package com.hinuri.linememoproject.entity

/**
 * 메모 상세 페이지에서 현재 메모의 상태를 구분
 */

enum class MemoState {
    MEMO_WRITE, // 새로 작성중인 경우
    MEMO_EDIT, // 편집하는 경우
    MEMO_EDIT_DONE, // 작성 및 편집 완료
    MEMO_VIEW, // 보기
    MEMO_DELETE_DONE // 삭제 완료
}