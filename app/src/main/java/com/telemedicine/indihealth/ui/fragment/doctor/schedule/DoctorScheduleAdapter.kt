package com.telemedicine.indihealth.ui.fragment.doctor.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemDoctorScheduleBinding
import com.telemedicine.indihealth.model.DoctorSchedule

class DoctorScheduleAdapter :
    RecyclerView.Adapter<DoctorScheduleAdapter.ViewHolder>() {

    private val items: MutableList<DoctorSchedule> by lazy {
        mutableListOf()
    }

    val clickedItem: MutableLiveData<DoctorSchedule> by lazy {
        MutableLiveData<DoctorSchedule>()
    }

    class ViewHolder(val binding: ItemDoctorScheduleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemDoctorScheduleBinding>(
                inflater,
                R.layout.item_doctor_schedule, parent, false
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
            doctorSchedule = item
//            root.setOnClickListener {
//                clickedItem.postValue(item)
//            }
        }
    }

    fun addList(list: List<DoctorSchedule>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}
