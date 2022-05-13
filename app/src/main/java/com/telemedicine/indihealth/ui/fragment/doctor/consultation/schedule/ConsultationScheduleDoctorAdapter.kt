package com.telemedicine.indihealth.ui.fragment.doctor.consultation.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemConsultationScheduleDoctorBinding
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation

class ConsultationScheduleDoctorAdapter :
    RecyclerView.Adapter<ConsultationScheduleDoctorAdapter.ViewHolder>() {

    private val items: MutableList<ScheduleDoctorConsultation> by lazy {
        mutableListOf()
    }

    val clickedItem: MutableLiveData<Event<ScheduleDoctorConsultation>> by lazy {
        MutableLiveData<Event<ScheduleDoctorConsultation>>()
    }

    val clickedCanceledItem: MutableLiveData<Event<ScheduleDoctorConsultation>> by lazy {
        MutableLiveData<Event<ScheduleDoctorConsultation>>()
    }

    class ViewHolder(val binding: ItemConsultationScheduleDoctorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemConsultationScheduleDoctorBinding>(
                inflater,
                R.layout.item_consultation_schedule_doctor, parent, false
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
            scheduleDoctorConsultation = item
            btnCall.setOnClickListener {
                clickedItem.postValue(Event(item))
            }
        }
    }

    fun addList(list: List<ScheduleDoctorConsultation>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}
