package com.telemedicine.indihealth.ui.fragment.doctor.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemMainMenuBinding
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.MenuKonsultasi
import timber.log.Timber


class MainDoctorMenuAdapter : RecyclerView.Adapter<MainDoctorMenuAdapter.ViewHolder>() {

    private val items: MutableList<MenuKonsultasi> by lazy {
        mutableListOf()
    }

    init {
        Timber.d("MenuKonsultasiAdapter is called")
    }

    val clickedItem: MutableLiveData<Event<MenuKonsultasi>> by lazy {
        MutableLiveData<Event<MenuKonsultasi>>()
    }

    class ViewHolder(val binding: ItemMainMenuBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemMainMenuBinding>(
                inflater,
                R.layout.item_main_menu, parent, false
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
            menuKonsultasi = item
            executePendingBindings()
            root.setOnClickListener {
                clickedItem.postValue(Event(item))
            }
        }
    }

    fun addList(list: List<MenuKonsultasi>) {
        Timber.d("MenuKonsultasiAdapter AddList = $list")
        items.addAll(list)
        notifyDataSetChanged()
    }
}
