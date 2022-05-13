package com.telemedicine.indihealth.ui.fragment.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutSplashScreenBinding
import com.telemedicine.indihealth.ui.activity.HomeActivity
import timber.log.Timber

class SplashScreenFragment : BaseFragment() {

    private val viewModel: SplashScreenViewModel by activityViewModels()
    private lateinit var navController: NavController
    private lateinit var mBinding: LayoutSplashScreenBinding
    private var extra:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = LayoutSplashScreenBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        Timber.d("LogOutStatus ${viewModel.getLogOutStatus()}")

        val bundle: Bundle? = arguments
        if (bundle!=null){
            Timber.d(bundle.getString("toOpen"))
            extra = bundle.getString("toOpen").toString()
        }

        if (viewModel.getLogOutStatus()) {
            navController.navigate(R.id.action_splashScreenFragment_to_getStartedFragment)
        } else {
            mBinding.container.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.splash_anim
                )
            )
            val handler = Handler(Looper.myLooper()!!)
                .postDelayed(
                    Runnable {
                        mBinding.container.visibility = View.GONE

                        when (viewModel.getLogInStatus()) {
                            "successpasien" -> {
                                startActivity(
                                    Intent(
                                        requireActivity(),
                                        HomeActivity::class.java
                                    )
                                )
                                requireActivity().finish()
                            }
                            "successdokter" -> {
                                navController
                                    .navigate(R.id.action_splashScreenFragment_to_mainDoctorFragment)
                            }
                            else -> {
                                if (viewModel.getBoardingStatus()) {
                                    navController.navigate(R.id.action_splashScreenFragment_to_getStartedFragment)
                                } else {
                                    navController.navigate(R.id.action_splashScreenFragment_to_onBoardingFragment)
                                }
                            }
                        }

                    }, 6000
                )
        }

    }
}