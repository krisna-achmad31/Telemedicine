package com.telemedicine.indihealth.ui.fragment.getstarted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.LayoutGetStartedBinding

class GetStartedFragment : Fragment() {

    private lateinit var mBinding: LayoutGetStartedBinding
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        mBinding.btnLogin.setOnClickListener {
            navController.navigate(R.id.action_getStartedFragment_to_loginFragment)
        }
        mBinding.btnRegister.setOnClickListener {
            navController.navigate(R.id.action_getStartedFragment_to_registrationFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = LayoutGetStartedBinding.inflate(inflater,container,false)
        return mBinding.root
    }

}