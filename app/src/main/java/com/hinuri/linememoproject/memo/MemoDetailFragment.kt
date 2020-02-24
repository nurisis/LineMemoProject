package com.hinuri.linememoproject.memo

/**
 * 메모 상세 보기 화면
 * */

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hinuri.linememoproject.databinding.FragmentMemoDetailBinding
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
