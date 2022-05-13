package com.telemedicine.indihealth.ui.activity.prescription

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemPrescriptionBinding
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.Prescription
import timber.log.Timber


class ConsultationPrescriptionAdapter : RecyclerView.Adapter<ConsultationPrescriptionAdapter.ViewHolder>() {

    val items: MutableList<Prescription> by lazy {
        mutableListOf()
    }

    val clickedItem: MutableLiveData<Event<Prescription>> by lazy {
        MutableLiveData<Event<Prescription>>()
    }

    init {
        Timber.d("MedicineAdapter is called")
    }

    class ViewHolder(val binding: ItemPrescriptionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemPrescriptionBinding>(
                inflater,
                R.layout.item_prescription, parent, false
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
            prescription = item
            executePendingBindings()
            root.setOnClickListener {
                clickedItem.value = Event(item)
            }
            itemPrescriptionIvClose.setOnClickListener {
                remove(item)
            }
        }
    }

    fun addList(list: List<Prescription>) {
        Timber.d("MedicineAdapter AddList = $list")
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun add(list: Prescription) {
        Timber.d("MedicineAdapter Add = $list")
        items.add(list)
        notifyDataSetChanged()
    }

    fun remove(list: Prescription) {
        Timber.d("MedicineAdapter remove = $list")
        items.remove(list)
        notifyDataSetChanged()
    }

    fun removeAll() {
        items.clear()
        notifyDataSetChanged()
    }
}
