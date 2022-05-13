package com.telemedicine.indihealth.ui.fragment.profile.doctor.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutProfileDoctorDetailBinding
import com.telemedicine.indihealth.model.ProfileDoctor
import com.telemedicine.indihealth.ui.fragment.profile.doctor.ProfileDoctorFragmentDirections
import kotlinx.android.synthetic.main.layout_profile_doctor_detail.*

class ProfileDoctorDetailFragment : BaseFragment() {

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_profileDoctorFragment_to_profileDoctorDetailFragment)
        }
        fun startFragmentDirection(view: View, profileDoctor: ProfileDoctor) {
            val action =
                ProfileDoctorFragmentDirections
                    .actionProfileDoctorFragmentToProfileDoctorDetailFragment(profileDoctor)
            view.findNavController().navigate(action)
        }
    }

    private val viewModel: ProfileDoctorDetailViewModel by activityViewModels()
    private val args: ProfileDoctorDetailFragmentArgs? by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutProfileDoctorDetailBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                profileDoctor = args?.profileDoctor
                vm = viewModel
                viewModel.profileDoctor = args?.profileDoctor
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        setObservableValue()
    }

    private fun setObservableValue() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })
        }
        // showViewModel.getProfile()
    }

    private fun setOnClickListener() {
        profile_doctor_detail_toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}