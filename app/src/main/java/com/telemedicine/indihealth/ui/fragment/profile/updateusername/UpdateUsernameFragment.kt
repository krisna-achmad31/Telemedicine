package com.telemedicine.indihealth.ui.fragment.profile.updateusername

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutUpdateUsernameBinding
import com.telemedicine.indihealth.helper.AppToast

class UpdateUsernameFragment : BaseFragment() {

    private val viewModel: UpdateUsernameViewModel by activityViewModels()

    private lateinit var mBinding: LayoutUpdateUsernameBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = LayoutUpdateUsernameBinding.inflate(inflater,container, false)
        return mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setOnClickListener()
        setObservableValue()
    }

    private fun setOnClickListener() {
        mBinding.profileEditToolbar.setNavigationOnClickListener {
            viewModel.resetPassword()
            requireActivity().onBackPressed()
        }
    }

    private fun setObservableValue() {
        viewModel.apply {

            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })

            isUpdateSuccess.observe(viewLifecycleOwner,{
                it.getContentIfNotHandled()?.apply {
                    if (this){
                        resetPassword()
                        requireActivity().onBackPressed()
                    }else{
                        AppToast.set(requireContext(),"Gagal merubah username!")
                    }
                }
            })

        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUsernameFromsharedPreference()
    }
}