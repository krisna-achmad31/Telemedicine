package com.telemedicine.indihealth.ui.fragment.doctor.assesment

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
import com.telemedicine.indihealth.databinding.LayoutDoctorAssesmentPatientBinding
import com.telemedicine.indihealth.ui.fragment.doctor.main.MainDoctorFragmentDirections
import kotlinx.android.synthetic.main.layout_doctor_assesment_patient.*
import timber.log.Timber

class DoctorAssesmentPatientFragment : BaseFragment() {

    private val viewModel: DoctorAssesmentPatientViewModel by activityViewModels()

    private val mAdapter: DoctorAssesmentPatientAdapter by lazy {
        DoctorAssesmentPatientAdapter()
    }

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainDoctorFragment_to_doctorAssesmentPatientFragment)
        }
        fun startFragmentDirection(fragment: Fragment) {
            val action =
                MainDoctorFragmentDirections
                    .actionMainDoctorFragmentToDoctorAssesmentPatientFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutDoctorAssesmentPatientBinding.inflate(inflater, container, false)
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
        doctor_assesment_patient_toolbar.setNavigationOnClickListener {
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
                spinner_assesment.adapter = adapter
                spinner_assesment?.setSelection(viewModel.positionName)
                spinner_assesment?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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
        }
    }

    private fun initQueueList() {
        viewModel.initQueue()
    }

}