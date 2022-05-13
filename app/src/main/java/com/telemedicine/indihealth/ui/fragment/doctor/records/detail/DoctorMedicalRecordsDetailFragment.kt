package com.telemedicine.indihealth.ui.fragment.doctor.records.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutDoctorMedicalRecordsDetailBinding
import com.telemedicine.indihealth.model.MedicalRecords
import com.telemedicine.indihealth.ui.fragment.doctor.records.DoctorMedicalRecordsFragment
import com.telemedicine.indihealth.ui.fragment.doctor.records.DoctorMedicalRecordsFragmentDirections
import kotlinx.android.synthetic.main.layout_doctor_medical_records_detail.*
import timber.log.Timber

class DoctorMedicalRecordsDetailFragment : BaseFragment() {

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_doctorMedicalRecordsFragment_to_doctorMedicalRecordsDetailFragment)
        }
        fun startFragmentDirection(view: View, medicalRecords: MedicalRecords) {
            val action =
                DoctorMedicalRecordsFragmentDirections
                    .actionDoctorMedicalRecordsFragmentToDoctorMedicalRecordsDetailFragment(medicalRecords)
            view.findNavController().navigate(action)
        }
    }

    private val viewModel: DoctorMedicalRecordsDetailViewModel by activityViewModels()
    private val args: DoctorMedicalRecordsDetailFragmentArgs? by navArgs()
    private val mAdapter: DoctorMedicalRecordsDetailAdapter by lazy {
        DoctorMedicalRecordsDetailAdapter()
    }
    private lateinit var mBinding: LayoutDoctorMedicalRecordsDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =  LayoutDoctorMedicalRecordsDetailBinding.inflate(inflater, container, false)
            return mBinding.apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel.medicalRecords = args?.medicalRecords
                viewModel.getQueue()
                medicalRecords = args?.medicalRecords
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
        medical_record_detail_toolbar.setNavigationOnClickListener {
            DoctorMedicalRecordsFragment.startFragmentDirection2(this@DoctorMedicalRecordsDetailFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        initQueueList()
    }

    private fun setObservableValue() {
        Timber.d("ROLE ${viewModel.getRole()}")
        if (viewModel.getRole() == "pasien"){
            mBinding.containerPatient.visibility = View.GONE
        }else{
            mBinding.containerDoctor.visibility = View.GONE
        }
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
        viewModel.getQueue()
    }

    private fun initQueueList() {
        viewModel.initQueue()
    }

}