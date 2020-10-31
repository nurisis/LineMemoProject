package com.hinuri.linememoproject.memolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hinuri.linememoproject.databinding.ItemMemoBinding
import com.hinuri.linememoproject.data.entity.Memo

class MemoListAdapter() : ListAdapter<Memo, MemoListAdapter.ViewHolder>(
    MemoListDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(private val binding: ItemMemoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Memo) {
            binding.apply {
                setItem(item)
                lifecycleOwner = binding.root.context as LifecycleOwner
            }.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                return ViewHolder(
                    ItemMemoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false)
                )
            }
        }
    }
}

private class MemoListDiffCallback : DiffUtil.ItemCallback<Memo>() {
    override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
        return oldItem == newItem
    }
}