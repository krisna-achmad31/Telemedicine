package com.telemedicine.indihealth.ui.fragment.schedule

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
import com.telemedicine.indihealth.databinding.LayoutScheduleDoctorBinding
import com.telemedicine.indihealth.ui.fragment.schedule.adapter.ScheduleDoctorAdapterFilterPoly
import kotlinx.android.synthetic.main.layout_schedule_doctor.*
import timber.log.Timber

class ScheduleDoctorFragment : BaseFragment() {

    private val viewModel: ScheduleDoctorViewModel by activityViewModels()
    private lateinit var mBinding: LayoutScheduleDoctorBinding
    private lateinit var filterPolyAdapter: ArrayAdapter<String>
    private var isResetPoly = false
    private var count=0


    private val mAdapter:  ScheduleDoctorAdapter by lazy {
        ScheduleDoctorAdapter()
    }

    private val mAdapterPoly: ScheduleDoctorAdapterFilterPoly by lazy {
        ScheduleDoctorAdapterFilterPoly()
    }

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainFragment_to_scheduleDoctorFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = LayoutScheduleDoctorBinding.inflate(inflater, container, false)
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
        setObservableValue()
        setOnClickListener()
//        setDropdown()
    }

    override fun onPause() {
        super.onPause()
        resetFilter()
    }

    override fun onResume() {
        super.onResume()
        initDoctorList()
    }

    private fun setObservableValue() {
        filterPolyAdapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            android.R.id.text1
        )
        filterPolyAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        filterPolyAdapter.clear()
        filterPolyAdapter.add("Pilih")

        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })

            consultationDoctorList.observe(viewLifecycleOwner,{
                if (it.isNotEmpty() && count < 1) {
                    setPolyList(it)
                    count++
                }
                viewModel.setConsultationDoctorListExist(it.isEmpty())
            })

            polyList.observe(viewLifecycleOwner,{
                for (row in it) {
                    filterPolyAdapter.add(row.name)
                }
                mBinding.poli.adapter = filterPolyAdapter
                mBinding.poli.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (parent!!.selectedItem.toString() != "Pilih") {
                            viewModel.condition["poli"] = parent.selectedItem.toString()
                            Timber.d("condition ${viewModel.condition}")
                            viewModel.getConsultationDoctor()
                        } else {
                            viewModel.condition["poli"] = ""
                            Timber.d("condition ${viewModel.condition}")
                            if (isResetPoly) {
                                isResetPoly = false
                            }else{
                                viewModel.getConsultationDoctor()
                            }
                        }
                    }
                }
            })
        }

        mAdapter.apply {
            clickedItem.observe(viewLifecycleOwner, {
//                DialogRegistrationConfirmation.newInstance(it, viewModel)
//                    ?.show(childFragmentManager, "")
            })
        }
    }

    private fun setOnClickListener() {
        schedule_doctor_toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        schedule_doctor_mcv_no_data.setOnClickListener {
            resetFilter()
            initDoctorList()
        }
    }

    private fun resetFilter() {
        mAdapterPoly.resetValues()
        isResetPoly=true
        mBinding.poli.setSelection(0)
        viewModel.condition = hashMapOf()
    }

    private fun initDoctorList() {
        viewModel.initConsultationDoctor()
    }

//    lateinit var poliAdapter : ArrayAdapter<String>
//
//    private fun setDropdown() {
//        poliAdapter = ArrayAdapter<String>(requireActivity() , android.R.layout.simple_spinner_item , android.R.id.text1)
//        poliAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
//        poliAdapter.add("Pilih")
//        poliAdapter.add("Bedah")
//        poliAdapter.add("Fisioterapi")
//
//        poli.adapter = poliAdapter
//        poli?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                if (parent!!.selectedItem.toString() != "Pilih") {
//                    viewModel.condition["poli"] = parent!!.selectedItem.toString()
//                    Timber.d("condition ${viewModel.condition}")
//                    viewModel.getConsultationDoctor()
//                }
//            }
//        }
//    }
}