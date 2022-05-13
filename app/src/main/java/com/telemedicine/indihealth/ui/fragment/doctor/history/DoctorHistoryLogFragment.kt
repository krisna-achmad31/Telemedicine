package com.telemedicine.indihealth.ui.fragment.doctor.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutDoctorHistoryLogBinding
import com.telemedicine.indihealth.ui.fragment.doctor.main.MainDoctorFragmentDirections
import kotlinx.android.synthetic.main.layout_doctor_history_log.*

class DoctorHistoryLogFragment : BaseFragment() {

    private lateinit var navController: NavController

    private lateinit var mBinding: LayoutDoctorHistoryLogBinding

    private val viewModel: DoctorHistoryLogViewModel by activityViewModels()

    private val mAdapter: DoctorHistoryLogAdapter by lazy {
        DoctorHistoryLogAdapter()
    }

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainDoctorFragment_to_doctorHistoryLogFragment)
        }
        fun startFragmentDirection(fragment: Fragment) {
            val action =
                MainDoctorFragmentDirections
                    .actionMainDoctorFragmentToDoctorHistoryLogFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = LayoutDoctorHistoryLogBinding.inflate(inflater, container, false)
        return mBinding
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                adapter = mAdapter
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setOnClickListener()
        setObservableValue()
        setDropdown()
    }

    private fun setOnClickListener() {
        doctor_history_log_toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        initQueueList()
    }

    private fun setObservableValue() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })
            queueList.observe(viewLifecycleOwner,{
                viewModel.setQueueListExist(it.isEmpty())
            })
        }
        mAdapter.apply {
            clickedItem.observe(viewLifecycleOwner, {

            })
        }
        viewModel.activity = "Panggilan Konsultasi"
        viewModel.getQueue()
    }

    private fun initQueueList() {
        viewModel.initQueue()
    }

    lateinit var activityAdapter : ArrayAdapter<String>

    private fun setDropdown() {
        activityAdapter = ArrayAdapter<String>(requireActivity() , android.R.layout.simple_spinner_item , android.R.id.text1)
        activityAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        activityAdapter.add("Panggilan Konsultasi")
        activityAdapter.add("Menerima Panggilan Konsultasi")
        activityAdapter.add("Konsultasi Berakhir")

        spinner_activity.adapter = activityAdapter
        spinner_activity?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.activity = parent!!.selectedItem.toString()
                viewModel.getQueue()
            }
        }
    }

}