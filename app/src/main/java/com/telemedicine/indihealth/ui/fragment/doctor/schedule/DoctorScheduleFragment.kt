package com.telemedicine.indihealth.ui.fragment.doctor.schedule

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
import com.telemedicine.indihealth.databinding.LayoutDoctorScheduleBinding
import com.telemedicine.indihealth.ui.fragment.doctor.main.MainDoctorFragmentDirections
import kotlinx.android.synthetic.main.layout_doctor_schedule.*

class DoctorScheduleFragment : BaseFragment() {

    private val viewModel: DoctorScheduleViewModel by activityViewModels()

    private val mAdapter: DoctorScheduleAdapter by lazy {
        DoctorScheduleAdapter()
    }

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainDoctorFragment_to_doctorScheduleFragment)
        }
        fun startFragmentDirection(fragment: Fragment) {
            val action =
                MainDoctorFragmentDirections
                    .actionMainDoctorFragmentToDoctorScheduleFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutDoctorScheduleBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                adapter = mAdapter
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        setObservableValue()
    }

    private fun setOnClickListener() {
        doctor_schedule_toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        initQueueList()
    }

    private fun setObservableValue() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })
            queueList.observe(viewLifecycleOwner,{
                viewModel.setQueueListExist(it.isEmpty())
            })
        }
        mAdapter.apply {
            clickedItem.observe(viewLifecycleOwner, {

            })
        }
        viewModel.getQueue()
    }

    private fun initQueueList() {
        viewModel.initQueue()
    }

}