package com.hinuri.linememoproject.memo

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
import com.hinuri.linememoproject.data.entity.Memo
import com.hinuri.linememoproject.databinding.FragmentMemoDetailBinding
import com.hinuri.linememoproject.databinding.FragmentWriteMemoBinding
import com.hinuri.linememoproject.entity.MemoState
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MemoDetailFragment : Fragment() {

    private val memoViewModel by sharedViewModel<MemoViewModel>()
    private lateinit var viewDataBinding : FragmentMemoDetailBinding
    private lateinit var imageListAdapter: ImageListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentMemoDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@MemoDetailFragment
            viewModel = memoViewModel
        }

        imageListAdapter = ImageListAdapter(memoViewModel)
        viewDataBinding.rvImage.apply {
            adapter = imageListAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        memoViewModel.memoImageList.observe(viewLifecycleOwner, Observer {
            imageListAdapter.submitList(it)
        })

    }

}
