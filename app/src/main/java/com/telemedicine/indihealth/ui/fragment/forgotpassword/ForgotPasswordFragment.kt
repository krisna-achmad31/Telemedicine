package com.telemedicine.indihealth.ui.fragment.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutForgotPasswordBinding
import com.telemedicine.indihealth.ui.fragment.login.LoginFragment
import com.telemedicine.indihealth.ui.fragment.login.LoginFragmentDirections
import kotlinx.android.synthetic.main.layout_login_pasien.*

class ForgotPasswordFragment : BaseFragment() {
    private val viewModel: ForgotPasswordViewModel by activityViewModels()


    companion object {
        fun startFragmentDirection(fragment: Fragment) {
            val action =
                LoginFragmentDirections
                    .actionLoginFragmentToForgotPasswordFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        return LayoutForgotPasswordBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservableValue()

    }

    private fun setObservableValue(){
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })
            message.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled()?.apply {
                    Toast.makeText(requireActivity(), this, Toast.LENGTH_SHORT).show()
                }
            })

            isSuccess.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled()?.apply {
                    if(this){
                        LoginFragment.startFragmentDirection3(this@ForgotPasswordFragment)
                    }
                }
            })
        }
    }

}