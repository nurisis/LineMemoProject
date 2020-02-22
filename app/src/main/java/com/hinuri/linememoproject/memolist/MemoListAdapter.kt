package com.nurisis.seemyclothappp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hinuri.linememoproject.databinding.ItemMemoBinding
import com.hinuri.linememoproject.data.entity.Memo
import com.hinuri.linememoproject.memolist.MemoListViewModel

class MemoListAdapter(private val viewModel: MemoListViewModel) : ListAdapter<Memo, MemoListAdapter.ViewHolder>(
    MemoListDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent,
            viewModel
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ItemMemoBinding, private val viewModel: MemoListViewModel) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Memo) {
            binding.apply {
                setItem(item)
            }.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, viewModel: MemoListViewModel) : ViewHolder {
                return ViewHolder(
                    ItemMemoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false),
                    viewModel
                )
            }
        }
    }
}

class MemoListDiffCallback : DiffUtil.ItemCallback<Memo>() {
    override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
        return oldItem == newItem
    }
}