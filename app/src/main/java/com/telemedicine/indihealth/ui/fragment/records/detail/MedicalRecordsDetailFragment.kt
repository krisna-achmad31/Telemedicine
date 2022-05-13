package com.telemedicine.indihealth.ui.fragment.records.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutMedicalRecordsDetailBinding
import com.telemedicine.indihealth.model.MedicalRecords
import com.telemedicine.indihealth.ui.fragment.records.MedicalRecordsFragmentDirections
import kotlinx.android.synthetic.main.layout_doctor_medical_records_detail.medical_record_detail_toolbar
import kotlinx.android.synthetic.main.layout_medical_records_detail.*
import timber.log.Timber

class MedicalRecordsDetailFragment : BaseFragment() {

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_medicalRecordsFragment_to_medicalRecordsDetailFragment)
        }
        fun startFragmentDirection(view: View, medicalRecords: MedicalRecords) {
            val action =
                MedicalRecordsFragmentDirections
                    .actionMedicalRecordsFragmentToMedicalRecordsDetailFragment(medicalRecords)
            view.findNavController().navigate(action)
        }
    }

    private val viewModel: MedicalRecordsDetailViewModel by activityViewModels()
    private val args: MedicalRecordsDetailFragmentArgs? by navArgs()
    private val mAdapter: MedicalRecordsDetailAdapter by lazy {
        MedicalRecordsDetailAdapter()
    }
    private lateinit var mBinding: LayoutMedicalRecordsDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = LayoutMedicalRecordsDetailBinding.inflate(inflater, container, false)
        return mBinding
            .apply {
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
            requireActivity().onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        initQueueList()
    }

    private fun setObservableValue() {
        Timber.d("ROLE ${viewModel.getRole()}")
        if (viewModel.getRole() == "pasien"){
            mBinding.containerPatient.visibility = GONE
        }else{
            mBinding.containerDoctor.visibility = GONE
        }

        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext(), cardView)
            })
            queueList.observe(viewLifecycleOwner,{
                viewModel.setQueueListExist(it.isEmpty())
            })
            orderStatus.observe(viewLifecycleOwner, {
                if(it.isNullOrEmpty()){
                    mBinding.tvOrderStatusPatient.visibility = GONE
                    mBinding.orderStatusPatient.visibility = GONE
                }else{
                    mBinding.tvOrderStatusPatient.visibility = VISIBLE
                    mBinding.orderStatusPatient.visibility = VISIBLE
                }
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