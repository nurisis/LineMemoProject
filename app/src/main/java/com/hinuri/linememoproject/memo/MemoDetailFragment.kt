package com.hinuri.linememoproject.memo

/**
 * 메모 상세 보기 화면
 * */

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hinuri.linememoproject.R
import com.hinuri.linememoproject.databinding.FragmentMemoDetailBinding
import kotlinx.android.synthetic.main.layout_memo_toolbar.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MemoDetailFragment : Fragment() {

    private val memoViewModel by sharedViewModel<MemoViewModel>()
    private lateinit var viewDataBinding : FragmentMemoDetailBinding
    private lateinit var imageListAdapter: ImageListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentMemoDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@MemoDetailFragment
            viewModel = memoViewModel
            toolbar.viewModel = memoViewModel
        }

        // 메모 내 이미지 리스트 관련 설정
        imageListAdapter = ImageListAdapter(memoViewModel)
        viewDataBinding.rvImage.apply {
            adapter = imageListAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }

        // 툴바의 뒤로가기 버튼 클릭
        viewDataBinding.toolbar.ivBack.setOnClickListener { activity?.finish() }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 메모 내 이미지 리스트 옵저빙
        memoViewModel.memoImageList.observe(viewLifecycleOwner, Observer {
            imageListAdapter.submitList(it)
        })

    }

}
