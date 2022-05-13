package com.telemedicine.indihealth.ui.fragment.queue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutQueueBinding
import com.telemedicine.indihealth.ui.bottomsheet.option.OptionBottomSheetFragment
import kotlinx.android.synthetic.main.layout_queue.*

class QueueFragment : BaseFragment() {

    private val viewModel: QueueViewModel by activityViewModels()

    private val mAdapter: QueueAdapter by lazy {
        QueueAdapter()
    }

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainFragment_to_queueFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutQueueBinding.inflate(inflater, container, false)
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
    }

    private fun setOnClickListener() {
        queue_toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        spinner_poli.setOnClickListener {
            val bottomSheetDialog = OptionBottomSheetFragment.newInstance(
                viewModel.polyDoctorList,
                viewModel.positionPoly,
                "Pilih Poli"
            )
            bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
            bottomSheetDialog.onClicked = {position ->
                viewModel.setSelectedPoli(position)
            }
        }
    }

    private fun initQueueList() {
        viewModel.initQueue()
    }
}