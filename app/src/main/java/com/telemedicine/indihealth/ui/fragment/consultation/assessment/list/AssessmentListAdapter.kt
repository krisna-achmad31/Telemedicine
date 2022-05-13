package com.telemedicine.indihealth.ui.fragment.consultation.assessment.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemConsultationAssessmentBinding
import com.telemedicine.indihealth.helper.SingleLiveData
import com.telemedicine.indihealth.model.Assessment
import timber.log.Timber

class AssessmentListAdapter :
    RecyclerView.Adapter<AssessmentListAdapter.ViewHolder>() {

    private val items: MutableList<Assessment> by lazy {
        mutableListOf()
    }

    val clickedItem: SingleLiveData<Assessment> by lazy {
        SingleLiveData()
    }

    class ViewHolder(val binding: ItemConsultationAssessmentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemConsultationAssessmentBinding>(
                inflater,
                R.layout.item_consultation_assessment, parent, false
            )
        return ViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        Timber.d("Consultation Items ${items.size}")
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            assessment = item
            button.setOnClickListener {
                clickedItem.setValue(item)
            }
        }
    }

    fun addList(list: List<Assessment>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}