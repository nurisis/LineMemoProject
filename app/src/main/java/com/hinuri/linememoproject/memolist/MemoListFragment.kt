package com.hinuri.linememoproject.memolist

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
import com.hinuri.linememoproject.databinding.FragmentMemoListBinding
import com.nurisis.seemyclothappp.ui.MemoListAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MemoListFragment : Fragment() {

    private val listViewModel by sharedViewModel<MemoListViewModel>()
    private lateinit var viewDataBinding : FragmentMemoListBinding
    private lateinit var listAdapter:MemoListAdapter

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
            Navigation.findNavController(viewDataBinding.root).navigate(R.id.action_memoListFragment_to_writeMemoFragment)
        }


        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        listViewModel.memoLists.observe(viewLifecycleOwner, Observer {
            listAdapter.submitList(it)
        })
    }

}
