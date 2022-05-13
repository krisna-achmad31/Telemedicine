package com.telemedicine.indihealth.ui.fragment.doctor.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.binding.setHtmlText
import com.telemedicine.indihealth.databinding.LayoutMainDoctorBinding
import com.telemedicine.indihealth.ui.fragment.doctor.assesment.DoctorAssesmentPatientFragment
import com.telemedicine.indihealth.ui.fragment.doctor.consultation.schedule.ConsultationScheduleDoctorFragment
import com.telemedicine.indihealth.ui.fragment.doctor.history.DoctorHistoryLogFragment
import com.telemedicine.indihealth.ui.fragment.doctor.profile.show.DoctorProfileShowFragment
import com.telemedicine.indihealth.ui.fragment.doctor.records.DoctorMedicalRecordsFragment
import com.telemedicine.indihealth.ui.fragment.doctor.schedule.DoctorScheduleFragment
import com.telemedicine.indihealth.ui.fragment.login.LoginFragmentDirections
import com.telemedicine.indihealth.ui.fragment.main.MainViewModel
import com.telemedicine.indihealth.ui.fragment.news.NewsFragment
import com.telemedicine.indihealth.ui.fragment.notification.NotificationFragment
import id.co.pradiptapaa.icare.binding.setImageUrlNews
import kotlinx.android.synthetic.main.item_news.view.*
import kotlinx.android.synthetic.main.layout_main_doctor.*
import kotlinx.android.synthetic.main.layout_main_menu.*


class MainDoctorFragment : BaseFragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var navController: NavController

    companion object {
        fun startFragment(view: View) {
            view.findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
        fun startFragmentDirection(fragment: Fragment) {
            val action =
                LoginFragmentDirections
                    .actionLoginFragmentToMainDoctorFragment()
            findNavController(fragment).navigate(action)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return LayoutMainDoctorBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                adapterConsultation = MainDoctorMenuAdapter().apply {
                    clickedItem.observe(viewLifecycleOwner,{
                        it.getContentIfNotHandled().apply {
                            when (this?.id){
                                "logout" -> {
                                    navController.navigate(R.id.action_mainDoctorFragment_to_getStartedFragment)
                                    viewModel.clearSharedPreference()
                                }
                                "data_konsultasi" -> {
                                    ConsultationScheduleDoctorFragment.startFragmentDirection(this@MainDoctorFragment)
                                }
                                "daftar" -> {
                                    DoctorProfileShowFragment.startFragmentDirection(this@MainDoctorFragment)
                                }
                                "jadwal_terdaftar" -> {
                                    DoctorScheduleFragment.startFragmentDirection(this@MainDoctorFragment)
                                }
                                "assesment_pasien" -> {
                                    DoctorAssesmentPatientFragment.startFragmentDirection(this@MainDoctorFragment)
                                }
                                "rekam_medis" -> {
                                    DoctorMedicalRecordsFragment.startFragmentDirection(this@MainDoctorFragment)
                                }
                                "histori_log" -> {
                                    DoctorHistoryLogFragment.startFragmentDirection(this@MainDoctorFragment)
                                }
                            }
                        }
                    })
                }
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setObservableValue()
        setOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.saveTokenNotification()
        viewModel.fetchUser()
        viewModel.getNotificationList()
        viewModel.getNotificationListUnread()
    }

    private fun setObservableValue(){
        viewModel.apply {
            newsList.observe(viewLifecycleOwner, {list ->
                main_doctor_carousel_news.setViewListener { //set view attributes here
                    val news = list[it]
                    val customView = layoutInflater.inflate(R.layout.item_news, null)
                    customView.apply {
                        item_news_tv_date.text = news.created_at
                        item_news_tv_title.text = news.judul
                        setImageUrlNews(item_news_iv_photo,news.foto)
                        setHtmlText(item_news_tv_description,news.berita)
                    }
                }
                main_doctor_carousel_news.setImageClickListener {
                    val news = list[it]
                    NewsFragment.startFragmentDirection(this@MainDoctorFragment,news)
                }
                main_doctor_carousel_news.pageCount = list.size
            })
        }
    }

    private fun setOnClickListener(){
        menu_utama_iv_doctor_notification.setOnClickListener {
            NotificationFragment.startFragmentDirection2(this,viewModel.notificationList.value)
        }
    }

}