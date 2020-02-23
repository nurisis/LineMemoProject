package com.hinuri.linememoproject.memolist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.hinuri.linememoproject.R
import com.hinuri.linememoproject.common.Constant
import com.hinuri.linememoproject.data.entity.Memo
import com.hinuri.linememoproject.databinding.FragmentMemoListBinding
import com.hinuri.linememoproject.memo.MemoActivity
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.Serializable

class MemoListFragment : Fragment() {

    private val listViewModel by sharedViewModel<MemoListViewModel>()
    private lateinit var viewDataBinding : FragmentMemoListBinding
    private lateinit var listAdapter: MemoListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = FragmentMemoListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@MemoListFragment
            viewModel = listViewModel
        }

        listAdapter = MemoListAdapter(listViewModel)
        viewDataBinding.rvMemo.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

        viewDataBinding.tvNoMemo.setOnClickListener {
            goToMemoActivity(Constant.EXTRA_MEMO_TYPE_WRITE)
        }
        viewDataBinding.fbAdd.setOnClickListener {
            goToMemoActivity(Constant.EXTRA_MEMO_TYPE_WRITE)
        }


        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        listViewModel.memoLists.observe(viewLifecycleOwner, Observer {
            listAdapter.submitList(it)
        })

        listViewModel.memoClicked.observe(viewLifecycleOwner, Observer {
            goToMemoActivity(Constant.EXTRA_MEMO_TYPE_VIEW, it)
        })
    }

    private fun goToMemoActivity(memoType:String, memoData:Memo? = null) {
        activity?.run {
            startActivity(
                Intent(this, MemoActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("type", memoType)
                    .apply {
                        memoData?.let { putExtra("memo", it) }
                    }
            )
        }
    }

}
