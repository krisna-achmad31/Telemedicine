package com.telemedicine.indihealth.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.binding.setHtmlText
import com.telemedicine.indihealth.databinding.LayoutMainMenuBinding
import com.telemedicine.indihealth.ui.fragment.main.adapter.MenuKonsultasiAdapter
import com.telemedicine.indihealth.ui.fragment.main.adapter.MenuLainnyaAdapter
import com.telemedicine.indihealth.ui.fragment.main.adapter.NewsAdapter
import com.telemedicine.indihealth.ui.fragment.news.NewsFragment
import id.co.pradiptapaa.icare.binding.setImageUrlNews
import kotlinx.android.synthetic.main.item_news.view.*
import kotlinx.android.synthetic.main.layout_main_doctor.*
import kotlinx.android.synthetic.main.layout_main_menu.*
import timber.log.Timber


class MainFragment : BaseFragment() {

    private lateinit var navController: NavController
    private val viewModel: MainViewModel by activityViewModels()
    private val extra:String = ""

    companion object {
        fun startFragment(view: View) {
            view.findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val bundle: Bundle? = arguments
        if (bundle!=null){
            Timber.d(bundle.getString("toOpen"))
        }

        return LayoutMainMenuBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                adapterMenuKonsultasi = MenuKonsultasiAdapter()
                adapterMenuLainnya = MenuLainnyaAdapter().apply {
                    clickedItem.observe(viewLifecycleOwner, {
                        it.getContentIfNotHandled().apply {
                            when (this?.id) {
                                "logout" -> {
                                    Navigation.findNavController(requireView()).navigate(R.id.action_mainFragment_to_loginFragment)
                                    viewModel.clearSharedPreference()
                                }
                            }
                        }
                    })
                }
                adapterNews = NewsAdapter()
                vm = viewModel
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservableValue()

    }

    override fun onResume() {
        super.onResume()
        viewModel.saveTokenNotification()
        viewModel.fetchUser()
        viewModel.getNotificationList()
        viewModel.getNotificationListUnread()
    }

    private fun setObservableValue() {
        viewModel.apply {
            newsList.observe(viewLifecycleOwner, { list ->
                main_menu_carousel_news.setViewListener { //set view attributes here
                    val news = list[it]
                    val customView = layoutInflater.inflate(R.layout.item_news, null)
                    customView.apply {
                        item_news_tv_date.text = news.getTanggal
                        item_news_tv_title.text = news.judul
                        setImageUrlNews(item_news_iv_photo, news.foto)
                        setHtmlText(item_news_tv_description, news.berita)
                    }
                }
                main_menu_carousel_news.setImageClickListener {
                    val news = list[it]
                    NewsFragment.startFragmentDirection2(this@MainFragment, news)
                }
                main_menu_carousel_news.pageCount = list.size
            })
        }
    }

}