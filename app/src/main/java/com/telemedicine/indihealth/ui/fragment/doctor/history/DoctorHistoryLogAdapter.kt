package com.telemedicine.indihealth.ui.fragment.doctor.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemDoctorHistoryLogBinding
import com.telemedicine.indihealth.model.DoctorHistoryLog

class DoctorHistoryLogAdapter :
    RecyclerView.Adapter<DoctorHistoryLogAdapter.ViewHolder>() {

    private val items: MutableList<DoctorHistoryLog> by lazy {
        mutableListOf()
    }

    val clickedItem: MutableLiveData<DoctorHistoryLog> by lazy {
        MutableLiveData<DoctorHistoryLog>()
    }

    class ViewHolder(val binding: ItemDoctorHistoryLogBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemDoctorHistoryLogBinding>(
                inflater,
                R.layout.item_doctor_history_log, parent, false
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
            doctorHistoryLog = item
            /*root.setOnClickListener {
                clickedItem.postValue(item)
            }*/
        }
    }

    fun addList(list: List<DoctorHistoryLog>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}
