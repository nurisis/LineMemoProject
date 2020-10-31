package com.hinuri.linememoproject.memolist

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hinuri.linememoproject.common.Constant
import com.hinuri.linememoproject.common.Constant.EXTRA_MEMO_DETAIL_KEY
import com.hinuri.linememoproject.common.util.SchedulerProvider
import com.hinuri.linememoproject.data.entity.Memo
import com.hinuri.linememoproject.databinding.FragmentMemoListBinding
import com.hinuri.linememoproject.domain.MemoUseCase
import com.hinuri.linememoproject.memo.MemoActivity
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MemoListFragment : Fragment(), MemoListContract.View {

    private lateinit var viewDataBinding : FragmentMemoListBinding
    private lateinit var listAdapter: MemoListAdapter
    private val memoUseCase: MemoUseCase by inject()
    // TODO :: presenter koin 이용해서 할 것.
    private val memoListPresenter:MemoListPresenter

    init {
        memoListPresenter = MemoListPresenter(
            memoUseCase,
            this,
            SchedulerProvider.instance!!
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = FragmentMemoListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@MemoListFragment
        }

        listAdapter = MemoListAdapter()
        viewDataBinding.rvMemo.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

        // 메모가 0개 일 때 보이는 텍스트뷰. 클릭 시 메모 작성 화면으로 이동
        viewDataBinding.tvNoMemo.setOnClickListener {
            goToMemoActivity(Constant.EXTRA_MEMO_TYPE_WRITE)
        }
        // 메모 작성 화면으로 이동
        viewDataBinding.fbAdd.setOnClickListener {
            goToMemoActivity(Constant.EXTRA_MEMO_TYPE_WRITE)
        }

        return viewDataBinding.root
    }

    override fun showMemoDetail(memo: Memo) {
    }

    override fun showMemoList(list: List<Memo>) {
        listAdapter.submitList(list)
    }

    override fun setPresenter(presenter: MemoListContract.Presenter) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        memoListPresenter.loadMemoList()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        listViewModel.memoLists.observe(viewLifecycleOwner, Observer {
//            listAdapter.submitList(it)
//        })
//
//        // 사용자가 메모 리스트 중 특정 메모를 클릭했을 때 옵저빙. 메모 상세 페이지로 이동
//        listViewModel.memoClicked.observe(viewLifecycleOwner, Observer {
//            goToMemoActivity(Constant.EXTRA_MEMO_TYPE_VIEW, it)
//        })
    }

    /**
     * 메모 상세(보기 또는 작성) 페이지로 이동
     * */
    private fun goToMemoActivity(memoType:String, memoData:Memo? = null) {
        activity?.run {
            startActivity(
                Intent(this, MemoActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra(Constant.EXTRA_MEMO_TYPE_KEY, memoType)
                    .apply {
                        memoData?.let { putExtra(EXTRA_MEMO_DETAIL_KEY, it) }
                    }
            )
        }
    }

}
