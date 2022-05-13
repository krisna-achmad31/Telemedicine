package com.telemedicine.indihealth.ui.fragment.consultation.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutConsultationScheduleBinding
import com.telemedicine.indihealth.ui.fragment.consultation.registration.ConsultationRegistrationFragment
import com.telemedicine.indihealth.ui.fragment.notification.NotificationFragmentDirections
import kotlinx.android.synthetic.main.layout_consultation_schedule.*

class ConsultationScheduleFragment : BaseFragment(){

    private val viewModel: ConsultationScheduleViewModel by activityViewModels()

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainFragment_to_consultationScheduleFragment)
        }

        fun startFragmentDirection2(fragment: Fragment) {
            val action =
                NotificationFragmentDirections
                    .actionNotificationFragmentToConsultationScheduleFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutConsultationScheduleBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                adapter = ConsultationScheduleAdapter()
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        initSchedule()
    }

    private fun setOnClickListener(){
//        consultation_schedule_mcv_no_data.setOnClickListener {
//            ConsultationRegistrationFragment.startFragmentDirection3(this)
//        }
        consultation_schedule_toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        button.setOnClickListener{
            ConsultationRegistrationFragment.startFragmentDirection3(this)
        }
    }

    private fun initSchedule(){
        viewModel.initSchedule()
    }

}