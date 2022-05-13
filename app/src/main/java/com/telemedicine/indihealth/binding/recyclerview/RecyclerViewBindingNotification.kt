package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.Notification
import com.telemedicine.indihealth.ui.fragment.notification.NotificationAdapter
import timber.log.Timber

@BindingAdapter("setAdapterNotificationList")
fun bindAdapterNotificationList(view: RecyclerView, list: List<Notification>?) {
    Timber.d("list payment = $list")
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? NotificationAdapter)?.addList(list)
    }
}