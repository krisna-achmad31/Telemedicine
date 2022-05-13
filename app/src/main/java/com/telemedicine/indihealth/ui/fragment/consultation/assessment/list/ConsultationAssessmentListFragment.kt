package com.telemedicine.indihealth.ui.fragment.consultation.assessment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutConsultationAssessmentListBinding
import timber.log.Timber

class ConsultationAssessmentListFragment : BaseFragment() {

    private lateinit var mBinding: LayoutConsultationAssessmentListBinding
    private val viewModel: ConsultationAssessmentListViewModel by activityViewModels()
    private lateinit var navController: NavController
    private val mAdapter: AssessmentListAdapter by lazy {
        AssessmentListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = LayoutConsultationAssessmentListBinding.inflate(inflater, container, false)
        return mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            adapter = mAdapter
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservables()
        setOnClickListeners()
        navController = Navigation.findNavController(view)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAssessmentList()
    }

    private fun setOnClickListeners() {
        mBinding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        mBinding.consultationAssessmentMcvNoData.setOnClickListener {
            viewModel.fetchAssessmentList()
        }
    }

    private fun setObservables() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })

            assessmentList.observe(viewLifecycleOwner,{
                Timber.d("Isi assesmen list $it")
                viewModel.setIsAssessmentLisExist(it.isEmpty())
            })
        }

        mAdapter.apply {
            clickedItem.observe(viewLifecycleOwner,{
                Timber.d("Clicked item $it")
                val bundle = bundleOf("idJadwalKonsultasi" to it.id_jadwal_konsultasi)
                navController.navigate(R.id.action_consultationAssessmentListFragment_to_consultationAssessmentFragment, bundle)
            })
        }
    }
}