package com.telemedicine.indihealth.ui.fragment.history

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutHistoryPaymentBinding
import com.telemedicine.indihealth.ui.fragment.history.consultation.HistoryConsultationAdapter
import com.telemedicine.indihealth.ui.fragment.history.drug.HistoryDrugAdapter
import com.telemedicine.indihealth.ui.fragment.notification.NotificationFragmentDirections
import kotlinx.android.synthetic.main.layout_history_payment.*
import timber.log.Timber


class HistoryPaymentFragment : BaseFragment() {

    private val viewModel: HistoryPaymentViewModel by activityViewModels()
    private lateinit var navController: NavController
    private lateinit var mBinding: LayoutHistoryPaymentBinding
    private lateinit var searchView: SearchView
    private var isInitial = true

    private val mAdapterConsultation: HistoryConsultationAdapter by lazy {
        HistoryConsultationAdapter()
    }

    private val mAdapterDrug: HistoryDrugAdapter by lazy {
        HistoryDrugAdapter()
    }

    lateinit var poliAdapter: ArrayAdapter<String>
    lateinit var paymentAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = LayoutHistoryPaymentBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).setSupportActionBar(mBinding.historyPaymentToolbar)
        return mBinding
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                adapterConsultation = mAdapterConsultation
                adapterDrug = mAdapterDrug
            }
            .root
    }

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainFragment_to_historyPaymentFragment)
        }

        fun startFragmentDirection(fragment: Fragment) {
            val action =
                NotificationFragmentDirections
                    .actionNotificationFragmentToHistoryPaymentFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setObservableValue()
//        setOnClickListener()
    }

//    private fun setOnClickListener() {
//        mBinding.historyPaymentToolbar.setNavigationOnClickListener {
//            if (searchView.isIconified){
//                requireActivity().onBackPressed()
//            }else{
//                searchView.isIconified=true
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        initHistoryConsultationList()
    }

    private fun setObservableValue() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })
//            historyConsultationList.observe(viewLifecycleOwner, {
//                viewModel.setHistoryConsultationListExist(it.isEmpty())
//            })
//            historyDrugList.observe(viewLifecycleOwner, {
//                viewModel.setHistoryDrugListExist(it.isEmpty())
//            })

            poliAdapter = ArrayAdapter<String>(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                android.R.id.text1
            )
            poliAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
            poliAdapter.add("Telekonsultasi")
            poliAdapter.add("Obat")

            poli.adapter = poliAdapter
            poli?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (parent?.selectedItem.toString() == "Telekonsultasi") {
                        viewModel.getHistoryConsultation()
                        history_rv_consultation.visibility = View.VISIBLE
                        history_rv_drug.visibility = View.GONE
                        history_payment_ll_no_data_drug.visibility = View.GONE
                        searchView.setQuery("", false)
                    } else {
                        viewModel.getHistoryDrug()
                        history_rv_consultation.visibility = View.GONE
                        history_rv_drug.visibility = View.VISIBLE
                        history_payment_ll_no_data_consultation.visibility = View.GONE
                        searchView.setQuery("", false)
                    }
                }
            }
            paymentAdapter = ArrayAdapter<String>(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                android.R.id.text1
            )
            paymentAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
            paymentAdapter.add("Semua")
            paymentAdapter.add("Transfer")
            paymentAdapter.add("Owlexa")

            payment.adapter = paymentAdapter
            payment?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (parent?.selectedItem.toString() == "Semua") {
                        viewModel.setPaymentFilter("")
                    } else  if (parent?.selectedItem.toString() == "Transfer"){
                        viewModel.setPaymentFilter("1")
                    } else if(parent?.selectedItem.toString() == "Owlexa"){
                        viewModel.setPaymentFilter("2")
                    }
                }
            }
        }
        mAdapterConsultation.apply {
            clickedItem.observe(viewLifecycleOwner, {
                it.id_jadwal?.let { idJadwal->
                    val bundle = bundleOf(
                        "idJadwal" to idJadwal.toInt(),
                        "idDokter" to it.id_dokter?.toInt()
                    )
                    navController.navigate(
                        R.id.action_historyPaymentFragment_to_consultationRegistrationConfirmationFragment,
                        bundle
                    )
                }?:Toast.makeText(requireContext(), "Konsultasi Ulang Gagal. Coba dijadwal lain", Toast.LENGTH_LONG).show()
            })
        }
    }

    private fun initHistoryConsultationList() {
        viewModel.initHistoryConsultation()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.search_menu, menu)
        val item = menu.findItem(R.id.searchMenu)
        searchView = SearchView(requireContext())
        item.actionView = searchView
        searchView.queryHint = "Nama dokter/obat"
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Timber.d("newtext $newText")
                mAdapterConsultation.filter.filter(newText)
                mAdapterDrug.filter.filter(newText)
                return true
            }

        })
        val searchEditId = androidx.appcompat.R.id.search_src_text
        val searchCloseId = androidx.appcompat.R.id.search_close_btn
        val et = searchView.findViewById<EditText>(searchEditId)
        val iv = searchView.findViewById<ImageView>(searchCloseId)
        iv.setImageResource(R.drawable.ic_baseline_close_24)
        et.setTextColor(Color.WHITE)
    }


}