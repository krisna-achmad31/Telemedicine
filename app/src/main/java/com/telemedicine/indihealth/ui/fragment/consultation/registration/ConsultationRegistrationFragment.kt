package com.telemedicine.indihealth.ui.fragment.consultation.registration

//import com.telemedicine.indihealth.ui.fragment.consultation.assessment.ConsultationAssessmentFragmentDirections
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutConsultationRegistrationBinding
import com.telemedicine.indihealth.ui.bottomsheet.option.OptionBottomSheetFragment
import com.telemedicine.indihealth.ui.dialog.DialogNotification
import com.telemedicine.indihealth.ui.fragment.consultation.payment.ConsultationPaymentFragment
import com.telemedicine.indihealth.ui.fragment.consultation.payment.ConsultationPaymentFragmentDirections
import com.telemedicine.indihealth.ui.fragment.consultation.schedule.ConsultationScheduleFragmentDirections
import kotlinx.android.synthetic.main.layout_consultation_registration.*
import kotlinx.android.synthetic.main.layout_history_payment.*
import timber.log.Timber
import java.util.*

class ConsultationRegistrationFragment : BaseFragment() {

    private val viewModel: ConsultationRegistrationViewModel by activityViewModels()
    private lateinit var searchView: SearchView
    private lateinit var mBinding: LayoutConsultationRegistrationBinding
    private lateinit var navController: NavController

    private var count: Int = 0
    private var isResetDay = false
    private var isResetPoly = false
    private var isInitial = true


    private val mAdapter: ConsultationRegistrationAdapter by lazy {
        ConsultationRegistrationAdapter()
    }

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainFragment_to_consultationRegistrationFragment)
        }

        fun startFragmentDirection(fragment: Fragment) {
            val action =
                ConsultationPaymentFragmentDirections
                    .actionConsultationPaymentFragmentToConsultationRegistrationFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }

        //        fun startFragmentDirection2(fragment: Fragment) {
//            val action =
//                ConsultationAssessmentFragmentDirections
//                    .actionConsultationAssessmentFragmentToConsultationRegistrationFragment()
//            NavHostFragment.findNavController(fragment).navigate(action)
//        }
        fun startFragmentDirection3(fragment: Fragment) {
            val action =
                ConsultationScheduleFragmentDirections
                    .actionConsultationScheduleFragmentToConsultationRegistrationFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = LayoutConsultationRegistrationBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).setSupportActionBar(mBinding.consultationRegistrationToolbar)
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
        setObservableValue()
        setOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        initDoctorList()
    }

    private fun setObservableValue() {
        val dayList = resources.getStringArray(R.array.dayList).toList()

//        filterDayAdapter = ArrayAdapter<String>(
//            requireActivity(),
//            android.R.layout.simple_spinner_item,
//            android.R.id.text1
//        )
//        filterDayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
//
//        filterPolyAdapter = ArrayAdapter<String>(
//            requireActivity(),
//            android.R.layout.simple_spinner_item,
//            android.R.id.text1
//        )
//        filterPolyAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)

        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })
            viewModel.setDayList(dayList)
            responseStatus.observe(viewLifecycleOwner, {
                Timber.d("responseStatus: ${it.peekContent()}")
                it.getContentIfNotHandled()?.apply {
                    when (this.getValue("status").toString()) {
                        "success" -> {
                            val dialog = DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                "Pendaftaran Konsultasi Berhasil",
                                this.getValue("msg").toString()
                            )
                            dialog?.show(childFragmentManager, "")
                            dialog?.apply {
                                onConfirmClicked = {
                                    ConsultationPaymentFragment.startFragmentDirection(
                                        this@ConsultationRegistrationFragment
                                    )
                                }
                            }
                        }
                        else -> {
                            DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                "Pendaftaran Konsultasi Gagal",
                                this.getValue("msg").toString()
                            )?.show(childFragmentManager, "")
                        }
                    }
                }
            })
            consultationDoctorList.observe(viewLifecycleOwner, {
                viewModel.setConsultationDoctorListExist(it.isEmpty())
                if (it.isNotEmpty() && count < 1) {
                    viewModel.setPolyList(it)
                    count++
                }
            })

//            this.polyList.observe(viewLifecycleOwner, {
//                filterPolyAdapter.clear()
//                filterPolyAdapter.add("Pilih")
//                for (row in it) {
//                    filterPolyAdapter.add(row.name)
//                }
//                mBinding.poli.adapter = filterPolyAdapter
//                mBinding.poli.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                    override fun onNothingSelected(parent: AdapterView<*>?) {
//
//                    }
//
//                    override fun onItemSelected(
//                        parent: AdapterView<*>?,
//                        view: View?,
//                        position: Int,
//                        id: Long
//                    ) {
//                        if (parent!!.selectedItem.toString() != "Pilih") {
//                            viewModel.condition["poli"] = parent.selectedItem.toString()
//                            Timber.d("condition ${viewModel.condition}")
//                            viewModel.getConsultationDoctor()
//                        } else {
//                            viewModel.condition["poli"] = ""
//                            Timber.d("condition ${viewModel.condition}")
//                            if (isResetPoly) {
//                                isResetPoly = false
//                            }else{
//                                viewModel.getConsultationDoctor()
//                            }
//                        }
//                    }
//                }
//            })

//            this.dayList.observe(viewLifecycleOwner, {
//                filterDayAdapter.clear()
//                filterDayAdapter.add("Pilih")
//                for (row in it) {
//                    filterDayAdapter.add(row.name)
//                }
//                mBinding.day.adapter = filterDayAdapter
//                mBinding.day.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                    override fun onNothingSelected(parent: AdapterView<*>?) {
//
//                    }
//
//                    override fun onItemSelected(
//                        parent: AdapterView<*>?,
//                        view: View?,
//                        position: Int,
//                        id: Long
//                    ) {
//                        if (parent!!.selectedItem.toString() != "Pilih") {
//                            viewModel.condition["hari"] = parent.selectedItem.toString()
//                            Timber.d("condition ${viewModel.condition}")
//                            viewModel.getConsultationDoctor()
//                        } else {
//                            viewModel.condition["hari"] = ""
//                            Timber.d("condition ${viewModel.condition}")
//                            if (isResetDay) {
//                                isResetDay=false
//                            }else{
//                                viewModel.getConsultationDoctor()
//                            }
//                        }
//
//                        if (!isInitial){
//
//                        }
//                    }
//                }
//            })
        }

        mAdapter.apply {
            clickedItem.observe(viewLifecycleOwner, {
//                DialogRegistrationConfirmation.newInstance(it, viewModel)
//                    ?.show(childFragmentManager, "")
                val bundle = bundleOf(
                    "idJadwal" to it.id,
                    "idDokter" to it.id_dokter
                )
                navController.navigate(R.id.action_consultationRegistrationFragment_to_consultationRegistrationConfirmationFragment,bundle)
            })
        }


/*        mAdapterPoly.apply {
            clickedItem.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled()?.apply {
                    viewModel.condition["poli"] = this
                    Timber.d("condition ${viewModel.condition}")
                    viewModel.getConsultationDoctor()
                }
            })
        }
        mAdapterDay.apply {
            clickedItem.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled()?.apply {
                    viewModel.condition["hari"] = this
                    Timber.d("condition ${viewModel.condition}")
                    viewModel.getConsultationDoctor()
                }
            })
        }*/
    }

    private fun setOnClickListener() {
        mBinding.consultationRegistrationToolbar.setNavigationOnClickListener {
            if (searchView.isIconified) {
                requireActivity().onBackPressed()
            } else {
                searchView.isIconified = true
            }
        }
        consultation_registration_mcv_no_data.setOnClickListener {
            isResetDay=true
            isResetPoly=true
            viewModel.setSelectedPoly(-1)
            viewModel.setSelectedDay(-1)
            initDoctorList()
        }

        mBinding.day.setOnClickListener {
            val bottomSheetDialog = OptionBottomSheetFragment.newInstance(
                viewModel.dayList,
                viewModel.daySelectedPosition,
                "Pilih Hari"
            )
            bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
            bottomSheetDialog.onClicked = {position ->
                viewModel.setSelectedDay(position)
                if (position != -1) {
                    Timber.d("condition ${viewModel.condition}")
                    viewModel.getConsultationDoctor()
                } else {
                    Timber.d("condition ${viewModel.condition}")
                    if (isResetDay) {
                        isResetDay=false
                    }else{
                        viewModel.getConsultationDoctor()
                    }
                }

                if (!isInitial){

                }
            }
        }

        mBinding.poli.setOnClickListener {
            val bottomSheetDialog = OptionBottomSheetFragment.newInstance(
                viewModel.polyList,
                viewModel.polySelectedPosition,
                "Pilih Poli"
            )
            bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
            bottomSheetDialog.onClicked = {position ->
                viewModel.setSelectedPoly(position)
                if (position != -1) {
                    Timber.d("condition ${viewModel.condition}")
                    viewModel.getConsultationDoctor()
                } else {
                    Timber.d("condition ${viewModel.condition}")
                    if (isResetPoly) {
                        isResetPoly = false
                    }else{
                        viewModel.getConsultationDoctor()
                    }
                }
            }
        }
    }

    private fun initDoctorList() {
        viewModel.initConsultationDoctor()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.search_menu, menu)
        val item = menu.findItem(R.id.searchMenu)
        searchView = SearchView(requireContext())
        item.actionView = searchView
        searchView.queryHint = "Nama dokter"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Timber.d("newtext $newText")
                mAdapter.filter.filter(newText)
                return true
            }

        })

        val searchEditId = androidx.appcompat.R.id.search_src_text
        val searchCloseId = androidx.appcompat.R.id.search_close_btn
//        val searchFrame = androidx.appcompat.R.id.searc
//        val sf = searchView.findViewById<View>(searchFrame)
//        sf.background = ResourcesCompat.getDrawable(resources, R.drawable.bottom_rounded_white_bg, null)
        val et = searchView.findViewById<EditText>(searchEditId)
        val iv = searchView.findViewById<ImageView>(searchCloseId)
        //searchView.background = ResourcesCompat.getDrawable(resources, R.drawable.bottom_rounded_white_bg, null)
        iv.setImageResource(R.drawable.ic_baseline_close_24)
        et.setTextColor(Color.WHITE)

    }

}