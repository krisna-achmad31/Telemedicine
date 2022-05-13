package com.telemedicine.indihealth.ui.fragment.doctor.records

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemDoctorMedicalRecordsBinding
import com.telemedicine.indihealth.model.MedicalRecords

class DoctorMedicalRecordsAdapter :
    RecyclerView.Adapter<DoctorMedicalRecordsAdapter.ViewHolder>() {

    private val items: MutableList<MedicalRecords> by lazy {
        mutableListOf()
    }

    val clickedItem: MutableLiveData<MedicalRecords> by lazy {
        MutableLiveData<MedicalRecords>()
    }

    class ViewHolder(val binding: ItemDoctorMedicalRecordsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemDoctorMedicalRecordsBinding>(
                inflater,
                R.layout.item_doctor_medical_records, parent, false
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
            medicalRecords = item
            button.setOnClickListener {
                clickedItem.postValue(item)
            }
        }
    }

    fun addList(list: List<MedicalRecords>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}
