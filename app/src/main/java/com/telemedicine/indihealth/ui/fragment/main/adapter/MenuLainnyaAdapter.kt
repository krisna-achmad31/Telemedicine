package com.telemedicine.indihealth.ui.fragment.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemOtherMenuBinding
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.MenuKonsultasi
import com.telemedicine.indihealth.ui.fragment.history.HistoryPaymentFragment
import com.telemedicine.indihealth.ui.fragment.profile.doctor.ProfileDoctorFragment
import com.telemedicine.indihealth.ui.fragment.profile.patient.show.ProfileShowFragment
import com.telemedicine.indihealth.ui.fragment.queue.QueueFragment
import com.telemedicine.indihealth.ui.fragment.recipe.RecipeDoctorFragment
import com.telemedicine.indihealth.ui.fragment.records.MedicalRecordsFragment
import com.telemedicine.indihealth.ui.fragment.schedule.ScheduleDoctorFragment
import timber.log.Timber


class MenuLainnyaAdapter : RecyclerView.Adapter<MenuLainnyaAdapter.ViewHolder>() {

    private val items: MutableList<MenuKonsultasi> by lazy {
        mutableListOf()
    }


    val clickedItem: MutableLiveData<Event<MenuKonsultasi>> by lazy {
        MutableLiveData<Event<MenuKonsultasi>>()
    }

    class ViewHolder(val binding: ItemOtherMenuBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemOtherMenuBinding>(
                inflater,
                R.layout.item_other_menu, parent, false
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
                when (item.id) {
                    "data_pasien" -> ProfileShowFragment.startFragment(it)
                    "profil_dokter" -> ProfileDoctorFragment.startFragment(it)
                    "cek_antrian" -> QueueFragment.startFragment(it)
                    "jadwal_dokter" -> ScheduleDoctorFragment.startFragment(it)
                    "resep_dokter" -> RecipeDoctorFragment.startFragment(it)
                    "rekam_medis" -> MedicalRecordsFragment.startFragment(it)
                    "history_pembayaran" -> HistoryPaymentFragment.startFragment(it)
                }
            }
        }
    }

    fun addList(list: List<MenuKonsultasi>) {
        Timber.d("MenuLainnyaAdapter AddList = $list")
        items.addAll(list)
        notifyDataSetChanged()
    }

}
