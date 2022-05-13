package com.telemedicine.indihealth.ui.fragment.toc

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutTocBinding
import com.telemedicine.indihealth.helper.AppToast
import com.telemedicine.indihealth.ui.activity.HomeActivity

class TocFragment : BaseFragment() {

    private lateinit var mBinding: LayoutTocBinding
    private lateinit var navController: NavController
    private val viewModel: TocViewModel by activityViewModels()
    private val args: TocFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = LayoutTocBinding.inflate(inflater, container, false)
        return mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setView()
    }

    private fun setView() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })

            toc.observe(viewLifecycleOwner) {
                val encodedHtml = Base64.encodeToString(it.toByteArray(), Base64.NO_PADDING)
                mBinding.webview.loadData(encodedHtml, "text/html", "base64")
            }
        }

        mBinding.buttonAcceptToc.setOnClickListener {
            findNavController().navigate(R.id.action_tocFragment_to_consultationRegistrationConfirmationFragment, Bundle().apply {
                putBoolean("isAccepted", true)
                putInt("idDokter", args.idDokter)
                putInt("idJadwal", args.idJadwal)
            })
        }
        mBinding.buttonDeclineToc.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}