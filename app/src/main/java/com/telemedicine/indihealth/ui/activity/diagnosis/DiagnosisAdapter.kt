package com.telemedicine.indihealth.ui.activity.diagnosis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemDiagnosisBinding
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.Diagnosis
import timber.log.Timber


class DiagnosisAdapter : RecyclerView.Adapter<DiagnosisAdapter.ViewHolder>() {

    private var items: MutableList<Diagnosis> = mutableListOf()
    private var itemList = items

    val clickedItem: MutableLiveData<Event<Diagnosis>> by lazy {
        MutableLiveData<Event<Diagnosis>>()
    }

    init {
        Timber.d("DiagnosisAdapter is called")
    }

    class ViewHolder(val binding: ItemDiagnosisBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemDiagnosisBinding>(
                inflater,
                R.layout.item_diagnosis, parent, false
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
            diagnosis = item
            executePendingBindings()
            root.setOnClickListener {
                clickedItem.value = Event(item)
            }
        }
    }

    fun addList(list: List<Diagnosis>) {
        items.clear()
        Timber.d("MedicineAdapter AddList = $list")
        items.addAll(list)
        notifyDataSetChanged()
    }

}
