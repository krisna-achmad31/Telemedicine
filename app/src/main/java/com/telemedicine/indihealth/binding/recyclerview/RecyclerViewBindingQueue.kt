package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.Queue
import com.telemedicine.indihealth.ui.fragment.queue.QueueAdapter
import timber.log.Timber

@BindingAdapter("setAdapterQueueList")
fun bindAdapterQueueList(view: RecyclerView, list: List<Queue>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? QueueAdapter)?.addList(list)
    }
}