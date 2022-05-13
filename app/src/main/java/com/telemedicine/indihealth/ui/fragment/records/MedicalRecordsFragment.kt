package com.telemedicine.indihealth.ui.fragment.records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutMedicalRecordsBinding
import com.telemedicine.indihealth.ui.fragment.records.detail.MedicalRecordsDetailFragment
import kotlinx.android.synthetic.main.layout_medical_records.*
import timber.log.Timber

class MedicalRecordsFragment : BaseFragment() {

    private val viewModel: MedicalRecordsViewModel by activityViewModels()

    private val mAdapter: MedicalRecordsAdapter by lazy {
        MedicalRecordsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutMedicalRecordsBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                adapter = mAdapter
            }
            .root
    }

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainFragment_to_medicalRecordsFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        setObservableValue()
    }

    override fun onResume() {
        super.onResume()
        initDoctorList()
    }

    private fun setOnClickListener() {
        consultation_registration_toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setObservableValue() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })
            profileDoctorList.observe(viewLifecycleOwner,{
                viewModel.setProfileDoctorListExist(it.isEmpty())
            })

            polyList.observe(viewLifecycleOwner,{
                val adapter = ArrayAdapter<String>(requireActivity() , android.R.layout.simple_spinner_item , android.R.id.text1)
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                Timber.d("it poli = $it")
                adapter.clear()
                adapter.add("Semua")
                var itemSelectedIndex = 0
                it.forEachIndexed { index, item ->
                    adapter.add(item)
                }
                spinner_poli.adapter = adapter
                spinner_poli?.setSelection(viewModel.positionPoly)
                spinner_poli?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (parent!!.selectedItem.toString() == "Semua") {
                            if(viewModel.poli != ""){
                                viewModel.poli = ""
                                viewModel.positionPoly = position
                                viewModel.getConsultationDoctor()
                            }
                        } else if(parent.selectedItem.toString() != viewModel.poli){
                            viewModel.poli = parent.selectedItem.toString()
                            viewModel.positionPoly = position
                            viewModel.getConsultationDoctor()
                        }
                    }
                }
            })
        }
        mAdapter.apply {
            clickedItem.observe(viewLifecycleOwner, {
                Timber.d("it clickedItem: $it")
                it.getContentIfNotHandled()?.apply {
                    MedicalRecordsDetailFragment.startFragmentDirection(requireView(), this)
                }
            })
        }
    }

    private fun initDoctorList() {
        viewModel.initConsultationDoctor()
    }

}