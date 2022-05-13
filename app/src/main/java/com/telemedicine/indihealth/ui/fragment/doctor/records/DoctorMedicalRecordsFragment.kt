package com.telemedicine.indihealth.ui.fragment.doctor.records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutDoctorMedicalRecordsBinding
import com.telemedicine.indihealth.ui.fragment.doctor.main.MainDoctorFragmentDirections
import com.telemedicine.indihealth.ui.fragment.doctor.records.detail.DoctorMedicalRecordsDetailFragment
import com.telemedicine.indihealth.ui.fragment.doctor.records.detail.DoctorMedicalRecordsDetailFragmentDirections
import kotlinx.android.synthetic.main.layout_doctor_medical_records.*
import timber.log.Timber

class DoctorMedicalRecordsFragment : BaseFragment() {

    private val viewModel: DoctorMedicalRecordsViewModel by activityViewModels()

    private val mAdapter: DoctorMedicalRecordsAdapter by lazy {
        DoctorMedicalRecordsAdapter()
    }

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainDoctorFragment_to_doctorMedicalRecordsFragment)
        }
        fun startFragmentDirection(fragment: Fragment) {
            val action =
                MainDoctorFragmentDirections
                    .actionMainDoctorFragmentToDoctorMedicalRecordsFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }
        fun startFragmentDirection2(fragment: Fragment) {
            val action =
                DoctorMedicalRecordsDetailFragmentDirections
                    .actionDoctorMedicalRecordsDetailFragmentToDoctorMedicalRecordsFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutDoctorMedicalRecordsBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                adapter = mAdapter
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        setObservableValue()
    }

    private fun setOnClickListener() {
        doctor_medical_records_toolbar.setNavigationOnClickListener {
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

            nameList.observe(viewLifecycleOwner,{
                val adapter = ArrayAdapter<String>(requireActivity() , android.R.layout.simple_spinner_item , android.R.id.text1)
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                Timber.d("it name = $it")
                adapter.clear()
                var itemSelectedIndex = 0
                it.forEachIndexed { index, item ->
                    adapter.add(item)
                }
                patient.adapter = adapter
                patient?.setSelection(viewModel.positionName)
                patient?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (parent!!.selectedItem.toString() != viewModel.name) {
                            viewModel.name = parent.selectedItem.toString()
                            viewModel.positionName = position
                            viewModel.getQueue()
                        }
                    }
                }
            })
            mAdapter.apply {
                clickedItem.observe(viewLifecycleOwner, {
                    Timber.d("it clickedItem: $it")
                    DoctorMedicalRecordsDetailFragment.startFragmentDirection(requireView(), it)
                })
            }
        }
    }

    private fun initQueueList() {
        viewModel.initQueue()
    }

}