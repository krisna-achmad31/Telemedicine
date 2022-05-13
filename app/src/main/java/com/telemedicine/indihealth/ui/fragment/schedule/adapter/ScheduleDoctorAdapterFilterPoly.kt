package com.telemedicine.indihealth.ui.fragment.schedule.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemConsultationRegistrationFilterBinding
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.FilterRegistrationDoctor
import timber.log.Timber


class ScheduleDoctorAdapterFilterPoly :
    RecyclerView.Adapter<ScheduleDoctorAdapterFilterPoly.ViewHolder>() {

    private val items: MutableList<FilterRegistrationDoctor> by lazy {
        mutableListOf()
    }

    val clickedItem: MutableLiveData<Event<String>> by lazy {
        MutableLiveData<Event<String>>()
    }

    private fun setClickedItem(string: String) {
        clickedItem.postValue(Event(string))
    }

    private var clickedPosition: Int = 9999

    init {
        Timber.d("MenuKonsultasiAdapter is called")
    }

    class ViewHolder(val binding: ItemConsultationRegistrationFilterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemConsultationRegistrationFilterBinding>(
                inflater,
                R.layout.item_consultation_registration_filter, parent, false
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
            root.setOnClickListener {
                if(clickedPosition == position){
                    clickedPosition = 99999
                    setClickedItem("")
                } else {
                    clickedPosition = position
                    setClickedItem(item.name)
                }
                notifyDataSetChanged()
            }
            if (clickedPosition == position) {
                itemConsultationRegistrationFilterTvName.setBackgroundColor(Color.parseColor("#03DAC5"))
                itemConsultationRegistrationFilterTvName.setTextColor(Color.parseColor("#ffffff"))
            } else {
                itemConsultationRegistrationFilterTvName.setBackgroundColor(Color.parseColor("#ffffff"))
                itemConsultationRegistrationFilterTvName.setTextColor(Color.parseColor("#8A000000"))
            }
            listItems = item

            executePendingBindings()
        }
    }

    fun addList(list: List<FilterRegistrationDoctor>) {
        Timber.d("MenuKonsultasiAdapter AddList = $list")
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun resetValues(){
        clickedPosition = 99999
        notifyDataSetChanged()
    }
}
