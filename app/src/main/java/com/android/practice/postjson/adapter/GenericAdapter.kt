package com.android.practice.postjson.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


abstract class GenericAdapter<T, VM : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<VM>() {

    private val list = ArrayList<T>()

    abstract fun createVm(parent: ViewGroup, viewType: Int): VM
    abstract fun bindVm(holder: VM, position: Int, item: T)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VM =
        createVm(parent, viewType)

    override fun onBindViewHolder(holder: VM, position: Int) {
        bindVm(holder, position, list[position])
    }

    override fun getItemCount() = list.size

    fun addItem(position: Int, item: T) {
        list.add(position, item)
        notifyDataSetChanged()
    }

    fun addAll(list: List<T>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        list.removeAt(position)
        notifyDataSetChanged()
    }

    fun remove(item: T) {
        list.remove(item)
        notifyDataSetChanged()
    }
}