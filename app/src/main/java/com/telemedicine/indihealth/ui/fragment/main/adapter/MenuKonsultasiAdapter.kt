package com.telemedicine.indihealth.ui.fragment.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemMainMenuBinding
import com.telemedicine.indihealth.model.MenuKonsultasi
import com.telemedicine.indihealth.ui.fragment.consultation.assessment.detail.ConsultationAssessmentFragment
import com.telemedicine.indihealth.ui.fragment.consultation.payment.ConsultationPaymentFragment
import com.telemedicine.indihealth.ui.fragment.consultation.registration.ConsultationRegistrationFragment
import com.telemedicine.indihealth.ui.fragment.consultation.schedule.ConsultationScheduleFragment
import timber.log.Timber


class MenuKonsultasiAdapter : RecyclerView.Adapter<MenuKonsultasiAdapter.ViewHolder>() {

    private val items: MutableList<MenuKonsultasi> by lazy {
        mutableListOf()
    }

    init {
        Timber.d("MenuKonsultasiAdapter is called")
    }

    private lateinit var navController: NavController

    class ViewHolder(val binding: ItemMainMenuBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        navController = Navigation.findNavController(parent)
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
                when (item.id) {
//                  "daftar" -> ConsultationAssessmentFragment.startFragment(it)
                    "daftar" -> ConsultationRegistrationFragment.startFragment(it)
                    "jadwal_terdaftar" -> ConsultationPaymentFragment.startFragment(it)
                    "assessment" -> navController.navigate(R.id.action_mainFragment_to_consultationAssessmentListFragment)
                    "jadwal_telekonsultasi" -> ConsultationScheduleFragment.startFragment(it)
                }
            }
        }
    }

    fun addList(list: List<MenuKonsultasi>) {
        Timber.d("MenuKonsultasiAdapter AddList = $list")
        items.addAll(list)
        notifyDataSetChanged()
    }
}
