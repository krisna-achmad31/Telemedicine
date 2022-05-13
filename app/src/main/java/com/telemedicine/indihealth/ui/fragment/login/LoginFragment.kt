package com.telemedicine.indihealth.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutLoginPasienBinding
import com.telemedicine.indihealth.helper.AppSnackbar
import com.telemedicine.indihealth.helper.AppToast
import com.telemedicine.indihealth.ui.activity.HomeActivity
import com.telemedicine.indihealth.ui.fragment.doctor.main.MainDoctorFragment
import com.telemedicine.indihealth.ui.fragment.doctor.main.MainDoctorFragmentDirections
import com.telemedicine.indihealth.ui.fragment.forgotpassword.ForgotPasswordFragment
import com.telemedicine.indihealth.ui.fragment.main.MainFragmentDirections
import com.telemedicine.indihealth.ui.fragment.registration.RegistrationFragment
import kotlinx.android.synthetic.main.layout_login_pasien.*
import timber.log.Timber

class LoginFragment : BaseFragment() {

    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var mBinding: LayoutLoginPasienBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setBoardingStatus()
        viewModel.setLogOutStatus()
    }

    companion object {
        fun startFragmentDirection(fragment: Fragment) {
            val action =
                MainFragmentDirections
                    .actionMainFragmentToLoginFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }

        fun startFragmentDirection1(fragment: Fragment) {
            val action =
                MainDoctorFragmentDirections
                    .actionMainDoctorFragmentToLoginFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }

        fun startFragmentDirection2(fragment: Fragment) {
            NavHostFragment.findNavController(fragment)
                .navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        fun startFragmentDirection3(fragment: Fragment) {
            NavHostFragment.findNavController(fragment)
                .navigate(R.id.action_forgotPasswordFragment_to_loginFragment)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        mBinding = LayoutLoginPasienBinding.inflate(inflater, container, false)
        return mBinding
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setObservableValue()
        setOnClickListener()

    }

    private fun setObservableValue() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })
            isLoggedIn.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled()?.apply {
                    Timber.d("User $this")
                    when (this) {
                        "successpasien" -> {
                            startActivity(Intent(requireActivity(), HomeActivity::class.java))
                            requireActivity().finish()
                        }
                        "successdokter" -> {
                            MainDoctorFragment.startFragmentDirection(this@LoginFragment)
                        }
                        else -> {
                            AppSnackbar.setError("User/Password Salah", requireView())
                        }
                    }
                }
            })
        }
    }

    private fun setOnClickListener() {
        textView3.setOnClickListener {
            RegistrationFragment.startFragmentDirection(this@LoginFragment)
        }
        textView2.setOnClickListener {
            ForgotPasswordFragment.startFragmentDirection(this@LoginFragment)
        }
        mBinding.button.setOnClickListener {
            viewModel.attemptLogin(
                onError = {
                    Timber.d("onError = $it")
                    AppSnackbar.setError(it, requireView())
                },
                onSuccess = {
                    Timber.d("success $it")
                    when (it) {
                        "successunread" -> {
                            navController.navigate(R.id.action_loginFragment_to_tocFragment)
                        }
                        "successdokter" -> {
                            navController.navigate(R.id.action_loginFragment_to_mainDoctorFragment)
                        }
                        "successpasien" -> {
                            startActivity(Intent(requireActivity(), HomeActivity::class.java))
                            requireActivity().finish()
                        }
                        else -> {
                            Timber.d("Login = $it ")
                            AppToast.set(requireContext(), it)
                        }
                    }
                }
            )
        }

    }

}