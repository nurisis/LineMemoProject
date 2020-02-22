package com.hinuri.linememoproject.memo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hinuri.linememoproject.databinding.FragmentWriteMemoBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WriteMemoFragment : Fragment() {

    private val memoViewModel by sharedViewModel<MemoViewModel>()
    private lateinit var viewDataBinding : FragmentWriteMemoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = FragmentWriteMemoBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@WriteMemoFragment
            viewModel = memoViewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}
