package com.telemedicine.indihealth.ui.fragment.profile.updatepassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutUpdatePasswordBinding
import com.telemedicine.indihealth.helper.AppToast

class UpdatePasswordFragment : BaseFragment() {

    private lateinit var mBinding: LayoutUpdatePasswordBinding
    private val viewModel: UpdatePasswordViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = LayoutUpdatePasswordBinding.inflate(inflater, container, false)
        return mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        setObservable()
    }

    private fun setObservable() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })

            isUpdateSuccess.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled()?.apply {
                    if (this == "Success") {
                        resetPassword()
                        AppToast.set(requireContext(), "Password Updated!")
                        requireActivity().onBackPressed()
                    } else {
                        AppToast.set(requireContext(), this)
                    }
                }
            })
        }
    }

    private fun setOnClickListener() {
        mBinding.profileEditToolbar.setNavigationOnClickListener {
            viewModel.resetPassword()
            requireActivity().onBackPressed()
        }
    }
}