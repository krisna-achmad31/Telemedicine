package com.telemedicine.indihealth.ui.fragment.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemScheduleDoctorBinding
import com.telemedicine.indihealth.model.ConsultationDoctor


class ScheduleDoctorAdapter :
    RecyclerView.Adapter<ScheduleDoctorAdapter.ViewHolder>() {

    private val items: MutableList<ConsultationDoctor> by lazy {
        mutableListOf()
    }

    val clickedItem: MutableLiveData<ConsultationDoctor> by lazy {
        MutableLiveData<ConsultationDoctor>()
    }

    class ViewHolder(val binding: ItemScheduleDoctorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemScheduleDoctorBinding>(
                inflater,
                R.layout.item_schedule_doctor, parent, false
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
            consultationDoctor = item
            root.setOnClickListener {
                clickedItem.postValue(item)
            }
        }
    }

    fun addList(list: List<ConsultationDoctor>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}
