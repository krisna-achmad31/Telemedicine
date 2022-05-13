package com.telemedicine.indihealth.ui.fragment.consultation.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemConsultationScheduleBinding
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.Payment
import com.telemedicine.indihealth.model.Schedule
import timber.log.Timber

class ConsultationScheduleAdapter :
    RecyclerView.Adapter<ConsultationScheduleAdapter.ViewHolder>() {


    val clickedItem: MutableLiveData<Event<Payment>> by lazy {
        MutableLiveData<Event<Payment>>()
    }

    val clickedCanceledItem: MutableLiveData<Event<Payment>> by lazy {
        MutableLiveData<Event<Payment>>()
    }

    private val items: MutableList<Schedule> by lazy {
        mutableListOf()
    }

    class ViewHolder(val binding: ItemConsultationScheduleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemConsultationScheduleBinding>(
                inflater,
                R.layout.item_consultation_schedule, parent, false
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
            schedule = item
            val jam = item.jam + " (Max. 30 Menit)"
            Timber.d(" pukul $jam")
            textView35.text = jam
        }
    }

    fun addList(list: List<Schedule>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}
