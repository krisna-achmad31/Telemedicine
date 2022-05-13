package com.telemedicine.indihealth.ui.fragment.doctor.profile.show

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutDoctorProfileShowBinding
import com.telemedicine.indihealth.ui.fragment.doctor.main.MainDoctorFragmentDirections

class DoctorProfileShowFragment : BaseFragment() {

    private val showViewModel: DoctorProfileShowViewModel by activityViewModels()
    private lateinit var mBinding : LayoutDoctorProfileShowBinding
    private lateinit var navController: NavController

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainDoctorFragment_to_doctorProfileShowFragment)
        }
        fun startFragmentDirection(fragment: Fragment) {
            val action =
                MainDoctorFragmentDirections
                    .actionMainDoctorFragmentToDoctorProfileShowFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = LayoutDoctorProfileShowBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).setSupportActionBar(mBinding.profileShowToolbar)
        return mBinding
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = showViewModel
            }
            .root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setOnClickListener()
        setObservableValue()
        showViewModel.fetchProfile()
    }

    private fun setObservableValue() {
        showViewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })
        }
    }

    private fun setOnClickListener() {
        mBinding.profileShowToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
//        edit_profile.setOnClickListener {
//            DoctorProfileEditFragment.startFragmentDirection(this@DoctorProfileShowFragment)
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.editMenu -> navController.navigate(R.id.action_doctorProfileShowFragment_to_doctorProfileEditFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}