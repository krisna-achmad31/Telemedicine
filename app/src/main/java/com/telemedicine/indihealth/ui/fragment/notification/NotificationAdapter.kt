package com.telemedicine.indihealth.ui.fragment.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemNotificationBinding
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.Notification
import timber.log.Timber


class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    private val items: MutableList<Notification> by lazy {
        mutableListOf()
    }

    val clickedItem: MutableLiveData<Event<Notification>> by lazy {
        MutableLiveData<Event<Notification>>()
    }

    init {
        Timber.d("NotificationAdapter is called")
    }

    class ViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemNotificationBinding>(
                inflater,
                R.layout.item_notification, parent, false
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
            notification = item
            executePendingBindings()
            root.setOnClickListener {
                clickedItem.postValue(Event(item))
//                val split = item.direct_link.split("/")
//                ConsultationScheduleDoctorFragment.startFragment(it)
//                when (item.id) {
//                    "doctor" -> DoctorLoginFragment.startFragment(it)
//                    "patient" -> PatientLoginFragment.startFragment(it)
//                }
            }
        }
    }

    fun addList(list: List<Notification>) {
        Timber.d("NotificationAdapter AddList = $list")
        items.addAll(list)
        notifyDataSetChanged()
    }
}
