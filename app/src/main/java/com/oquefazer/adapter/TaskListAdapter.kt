package com.oquefazer.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oquefazer.R
import com.oquefazer.data.local.WhatsToDo
import com.oquefazer.databinding.ListitemBinding
import com.oquefazer.databinding.SelectedItemListBinding
import com.oquefazer.extencions.convertDataToLong
import com.oquefazer.extencions.format
import com.oquefazer.extencions.text
import java.util.Date
import java.util.TimeZone

class TaskListAdapter(
    private val onTaskClick : (WhatsToDo) -> Unit
) : ListAdapter<WhatsToDo, TaskListAdapter.TaskViewHolder>(DiffCallback()) {

    var listenerDelete : (WhatsToDo) -> Unit = {}
    private var isEnable = false
    private val itemSelectedList = mutableListOf<Int>()
    companion object {
        const val normalView = 1
        const val selectedView = 2
    }

    inner class  TaskViewHolder(private val binding: ListitemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind (item : WhatsToDo) {
            binding.txtTitleItem.text = item.title
            if (item.hour!! <= 3) {
                binding.txtDataItem.visibility = View.GONE
            } else {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.txtDataItem.text = Date(item.hour!! + offset).format()
            }
            binding.txtDescriptionItem.text = item.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        /*return when(viewType) {
            normalView -> ListitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            selectedView -> SelectedItemListBinding.inflate(inflater, parent, false)
            else {}
        }*/
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListitemBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.itemView.setOnClickListener {
            onTaskClick(task)
        }
        holder.itemView.setOnLongClickListener {
            if (itemSelectedList.contains(position)) {
                itemSelectedList.remove(position)
            } else {
                selectItem(holder, task, position)
                Log.i("adapter", itemSelectedList.toString())
            }
            true
        }
        holder.bind(task)

    }

    private fun selectItem(
        holder: TaskViewHolder,
        task: WhatsToDo?,
        position: Int
    ) {

        isEnable = true
        itemSelectedList.add(position)
    }

    class DiffCallback : DiffUtil.ItemCallback<WhatsToDo>() {
        override fun areItemsTheSame(oldItem: WhatsToDo, newItem: WhatsToDo): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: WhatsToDo, newItem: WhatsToDo): Boolean = oldItem == newItem
    }
}

