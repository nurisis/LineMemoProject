package com.hinuri.linememoproject.memo

/**
 * 메모 상세 (보기 또는 작성,편집) 화면
 * 메모 상세 보기일 경우 => MemoDetailFragment
 * 메모 작성 및 편집일 경우 => WriteMemoFragment
 */

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.hinuri.linememoproject.R
import com.hinuri.linememoproject.common.Constant
import com.hinuri.linememoproject.common.Constant.EXTRA_MEMO_DETAIL_KEY
import com.hinuri.linememoproject.data.entity.Memo
import com.hinuri.linememoproject.entity.MemoState
import org.koin.androidx.viewmodel.ext.android.viewModel

class MemoActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private val memoViewModel by viewModel<MemoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        navController = Navigation.findNavController(this,
            R.id.fragment_nav_memo
        )
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)

        intent?.apply {
            when(getStringExtra(Constant.EXTRA_MEMO_TYPE_KEY)) {
                // 상세 보기의 경우 => 리스트에서 메모 데이터를 받아옴.
                Constant.EXTRA_MEMO_TYPE_VIEW -> {
                    memoViewModel.setMemo(getSerializableExtra(EXTRA_MEMO_DETAIL_KEY) as Memo)
                }
                // 메모 작성의 경우
                Constant.EXTRA_MEMO_TYPE_WRITE -> {
                    memoViewModel.changeMemoState(MemoState.MEMO_WRITE)
                }
            }
        }

        // MemoState를 옵저빙 후 그에 맞게 처리해줌. 즉, 그에 맞게 화면 이동 해줌.
        memoViewModel.memoStatus.observe(this, Observer {
            when(it){
                MemoState.MEMO_WRITE, MemoState.MEMO_EDIT -> navController.navigate(R.id.action_memoDetailFragment_to_writeMemoFragment2)
                MemoState.MEMO_EDIT_DONE -> navController.navigate(R.id.action_writeMemoFragment_to_memoDetailFragment)
                MemoState.MEMO_DELETE_DONE -> finish()
            }
        })
    }

    override fun onBackPressed() {
        supportFragmentManager.run {
            // Fragment stack 없을 시 activity 종료
            if(backStackEntryCount < 1) finish()
            else popBackStack()
        }
    }
}
