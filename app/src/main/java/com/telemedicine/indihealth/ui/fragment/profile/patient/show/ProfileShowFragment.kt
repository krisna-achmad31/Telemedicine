package com.telemedicine.indihealth.ui.fragment.profile.patient.show

import android.content.Intent
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
import com.telemedicine.indihealth.databinding.LayoutProfilePatientShowBinding
import com.telemedicine.indihealth.ui.activity.MainActivity
import com.telemedicine.indihealth.ui.fragment.main.MainFragmentDirections
import kotlinx.android.synthetic.main.layout_profile_patient_show.*


class ProfileShowFragment : BaseFragment() {

    private lateinit var navController: NavController
    private lateinit var mBinding: LayoutProfilePatientShowBinding
    private val showViewModel: ProfileShowViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setOnClickListener()
        setObservableValue()
        showViewModel.fetchProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = LayoutProfilePatientShowBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).setSupportActionBar(mBinding.profileShowToolbar)
        return mBinding
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = showViewModel
            }
            .root
    }

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainFragment_to_profileShowFragment)
        }

        fun startFragmentDirection(fragment: Fragment) {
            val action =
                MainFragmentDirections
                    .actionMainFragmentToProfileShowFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }
    }

    private fun setObservableValue() {
        showViewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun setOnClickListener() {
        button.setOnClickListener {
            showViewModel.clearSharedPreference()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.editMenu -> navController.navigate(R.id.action_profileShowFragment_to_profileEditFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}