package com.telemedicine.indihealth.ui.fragment.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutNewsDetailBinding
import com.telemedicine.indihealth.model.News
import com.telemedicine.indihealth.ui.fragment.doctor.main.MainDoctorFragmentDirections
import com.telemedicine.indihealth.ui.fragment.main.MainFragmentDirections
import kotlinx.android.synthetic.main.layout_news_detail.*

class NewsFragment : BaseFragment() {

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainFragment_to_consultationAssessmentFragment)
        }

        fun startFragmentDirection(fragment: Fragment, news: News) {
            val action =
                MainDoctorFragmentDirections
                    .actionMainDoctorFragmentToNewsFragment(news)
            NavHostFragment.findNavController(fragment).navigate(action)
        }
        fun startFragmentDirection2(fragment: Fragment, news: News) {
            val action =
                MainFragmentDirections
                    .actionMainFragmentToNewsFragment(news)
            NavHostFragment.findNavController(fragment).navigate(action)
        }
    }

    val args: NewsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutNewsDetailBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                news = args.news
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservableValue()
        setOnClickListener()
    }

    private fun setObservableValue() {

    }

    private fun setOnClickListener() {
        news_detail_toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }


}