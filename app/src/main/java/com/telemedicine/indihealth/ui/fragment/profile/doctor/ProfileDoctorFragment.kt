package com.telemedicine.indihealth.ui.fragment.profile.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutProfileDoctorBinding
import com.telemedicine.indihealth.ui.fragment.profile.doctor.detail.ProfileDoctorDetailFragment
import kotlinx.android.synthetic.main.layout_profile_doctor.*
import timber.log.Timber

class ProfileDoctorFragment : BaseFragment() {

    private val viewModel: ProfileDoctorViewModel by activityViewModels()

    private val mAdapter: ProfileDoctorAdapter by lazy {
        ProfileDoctorAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutProfileDoctorBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                adapter = mAdapter
            }
            .root
    }

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainFragment_to_profileDoctorFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        setObservableValue()
    }

    override fun onPause() {
        super.onPause()
        resetFilter()
    }

    override fun onResume() {
        super.onResume()
        initDoctorList()
    }

    private fun setOnClickListener() {
        consultation_registration_toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        consultation_registration_ll_no_data.setOnClickListener {
            resetFilter()
            initDoctorList()
        }
    }

    private fun setObservableValue() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })
            profileDoctorList.observe(viewLifecycleOwner,{
                viewModel.setProfileDoctorListExist(it.isEmpty())
            })
        }
        mAdapter.apply {
            clickedItem.observe(viewLifecycleOwner, {
                Timber.d("it clickedItem: $it")
                it.getContentIfNotHandled()?.apply {
                    ProfileDoctorDetailFragment.startFragmentDirection(requireView(), this)
                }
            })
        }
        viewModel.getConsultationDoctor()
    }

    private fun resetFilter() {
        viewModel.condition = hashMapOf()
    }

    private fun initDoctorList() {
        viewModel.initConsultationDoctor()
    }
}