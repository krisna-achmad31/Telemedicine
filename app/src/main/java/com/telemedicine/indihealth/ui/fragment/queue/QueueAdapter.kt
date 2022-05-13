package com.telemedicine.indihealth.ui.fragment.queue

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemQueueBinding
import com.telemedicine.indihealth.model.Queue


class QueueAdapter :
    RecyclerView.Adapter<QueueAdapter.ViewHolder>() {

    private val items: MutableList<Queue> by lazy {
        mutableListOf()
    }

    val clickedItem: MutableLiveData<Queue> by lazy {
        MutableLiveData<Queue>()
    }

    class ViewHolder(val binding: ItemQueueBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemQueueBinding>(
                inflater,
                R.layout.item_queue, parent, false
            )
        return ViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            queue = item
//            root.setOnClickListener {
//                clickedItem.postValue(item)
//            }
        }
    }

    fun addList(list: List<Queue>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}
