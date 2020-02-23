package com.hinuri.linememoproject.memo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.hinuri.linememoproject.R
import com.hinuri.linememoproject.common.Constant
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
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        intent?.apply {
            when(getStringExtra("type")) {
                Constant.EXTRA_MEMO_TYPE_VIEW -> {
                    memoViewModel.setMemo(getSerializableExtra("memo") as Memo)
                }
                Constant.EXTRA_MEMO_TYPE_WRITE -> {
                    memoViewModel.changeMemoState(MemoState.MEMO_WRITE)
                }
            }
        }

        memoViewModel.memoStatus.observe(this, Observer {
            when(it){
                MemoState.MEMO_WRITE -> navController.navigate(R.id.action_memoDetailFragment_to_writeMemoFragment2)
                MemoState.MEMO_EDIT -> navController.navigate(R.id.action_memoDetailFragment_to_writeMemoFragment2)
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
