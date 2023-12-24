package com.oquefazer.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.oquefazer.data.local.WhatsToDo
import com.oquefazer.databinding.SelectedItemListBinding
import com.oquefazer.extencions.format
import java.util.Date
import java.util.TimeZone

class ItemSelectedAdapter(private val binding: SelectedItemListBinding) : RecyclerView.ViewHolder(binding.root) {

    class  TaskViewHolder(private val binding: SelectedItemListBinding) : RecyclerView.ViewHolder(binding.root) {
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
}