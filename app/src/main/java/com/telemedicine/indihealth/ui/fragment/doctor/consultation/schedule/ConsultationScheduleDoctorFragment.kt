package com.telemedicine.indihealth.ui.fragment.doctor.consultation.schedule

import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutConsultationScheduleDoctorBinding
import com.telemedicine.indihealth.ui.activity.doctor.ConsultationCallFragment
import com.telemedicine.indihealth.ui.fragment.doctor.main.MainDoctorFragmentDirections
import com.telemedicine.indihealth.ui.fragment.notification.NotificationFragmentDirections
import kotlinx.android.synthetic.main.layout_consultation_schedule_doctor.*
import timber.log.Timber


class ConsultationScheduleDoctorFragment : BaseFragment() {

    private val viewModel: ConsultationScheduleDoctorViewModel by activityViewModels()

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainDoctorFragment_to_consultationScheduleDoctorFragment)
        }

        fun startFragmentDirection(fragment: Fragment) {
            val action =
                MainDoctorFragmentDirections
                    .actionMainDoctorFragmentToConsultationScheduleDoctorFragment()
            findNavController(fragment).navigate(action)
        }

        fun startFragmentNotification(fragment: Fragment) {
            val action =
                NotificationFragmentDirections
                    .actionNotificationFragmentToConsultationScheduleDoctorFragment()
            findNavController(fragment).navigate(action)
        }

        fun pendingIntent(context: Context): PendingIntent {
            return NavDeepLinkBuilder(context)
                .setGraph(R.navigation.navigation_main)
                .setDestination(R.id.consultationScheduleDoctorFragment)
                .createPendingIntent()
        }

    }

    private var mAdapter: ConsultationScheduleDoctorAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAdapter = ConsultationScheduleDoctorAdapter()
        return LayoutConsultationScheduleDoctorBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                adapter = mAdapter
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservableValue()
        setOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getScheduleDoctor()
    }

    override fun onPause() {
        super.onPause()
        mAdapter = null
    }

    private fun setObservableValue() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                Timber.d("isLoading = ${it.peekContent()}")
                loadingValidation(it, requireContext())
            })
        }
        mAdapter?.apply {
            clickedItem.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled().apply {
                    ConsultationCallFragment.startActivity(requireContext(),this)
                }
            })
        }
    }

    private fun setOnClickListener() {
        consultation_schedule_doctor_toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        consultation_schedule_doctor_mcv_no_data.setOnClickListener {
            viewModel.getScheduleDoctor()
        }
    }


}