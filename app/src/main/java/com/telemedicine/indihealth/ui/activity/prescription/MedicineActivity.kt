package com.telemedicine.indihealth.ui.activity.prescription

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseActivity
import com.telemedicine.indihealth.databinding.LayoutMedicineBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_medicine.*

@AndroidEntryPoint
class MedicineActivity : BaseActivity(){
    private val binding: LayoutMedicineBinding by binding(R.layout.layout_medicine)
    private val viewModel: MedicineViewModel by viewModels()

    private val mAdapter: MedicineAdapter by lazy {
        MedicineAdapter()
    }

    companion object{
        const val REQ_OBAT = 1
        fun startActivityWithResult(activity: Activity){
            val intent = Intent(activity, MedicineActivity::class.java)
            activity.startActivityForResult(intent, REQ_OBAT)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@MedicineActivity
            adapter = mAdapter
            vm = viewModel
        }
        setObservableValue()
        setOnClickListener()
    }

    private fun setOnClickListener(){
        medicine_toolbar.setOnClickListener {
            finish()
        }

        binding.medicineSv.setOnClickListener {
            binding.medicineSv.isIconified = false
        }
        medicine_sv.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                mAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                mAdapter.filter.filter(query)
                return false
            }
        })    }

    private fun setObservableValue(){
        mAdapter.apply {
            clickedItem.observe(this@MedicineActivity,{
                it?.getContentIfNotHandled().apply {
                    val intent = Intent()
                    intent.putExtra("obat", this)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchMedicineList()
    }
}