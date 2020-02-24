package com.hinuri.linememoproject.memo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hinuri.linememoproject.CoroutinesTestRule
import com.hinuri.linememoproject.domain.MemoUseCase
import com.hinuri.linememoproject.entity.MemoState
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.reset
import org.mockito.MockitoAnnotations

class MemoViewModelTest {
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // test 내에서 viewModelScope 과 같이 코루틴 테스트 할 경우 아래 rule이 있어야 테스트 가능.
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val memoUseCase = Mockito.mock(MemoUseCase::class.java)
    private val viewModel by lazy { MemoViewModel(memoUseCase) }

    @Before
    fun setUp() {
        reset(memoUseCase)

        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `memoState should be EDIT_DONE after saving memo`() {
        viewModel.changeMemoState(MemoState.MEMO_WRITE)

        viewModel.saveMemo("TITLE", "CONTENT")
        assertTrue(viewModel.memoStatus.value == MemoState.MEMO_EDIT_DONE)
    }

    @Test
    fun `number of images should be decrease after delete an image`() {
        viewModel.changeMemoState(MemoState.MEMO_WRITE) // 초기화
        viewModel.addImage("IMAGE1")
        viewModel.addImage("IMAGE2")

        val beforNumOfImages = viewModel.memoImageList.value!!.size

        viewModel.deleteImage("IMAGE1")

        assertTrue(beforNumOfImages-1 == viewModel.memoImageList.value!!.size)
    }
}