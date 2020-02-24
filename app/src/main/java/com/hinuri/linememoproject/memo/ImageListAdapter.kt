package com.hinuri.linememoproject.memo

/**
 * 메모에서 n개의 이미지를 보여주는 어뎁터
 * */

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hinuri.linememoproject.databinding.ItemImageBinding

class ImageListAdapter(private val viewModel: MemoViewModel) : ListAdapter<String, ImageListAdapter.ViewHolder>(
    ImageListDiffCallback()
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

    class ViewHolder private constructor(private val binding: ItemImageBinding, private val viewModel: MemoViewModel) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imagePath: String) {
            binding.apply {
                setImagePath(imagePath)
                viewModel = this@ViewHolder.viewModel
                lifecycleOwner = binding.root.context as LifecycleOwner
            }.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, viewModel: MemoViewModel) : ViewHolder {
                return ViewHolder(
                    ItemImageBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false),
                    viewModel
                )
            }
        }
    }
}

private class ImageListDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}