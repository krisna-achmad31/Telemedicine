package com.telemedicine.indihealth.ui.fragment.notification

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.Notification
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.repo.MainRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class NotificationViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

    val user: User? by lazy {
        sharedPreference.getUserValue()
    }

    val notificationList: MutableLiveData<List<Notification>> by lazy {
        MutableLiveData<List<Notification>>()
    }

    fun postNotificationReadAll() {
        setLoading(true)
        val param = hashMapOf<String?,String?>(
            "id_user" to sharedPreference.getUserValue()?.id
        )
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.postNotificationReadAll(
                param,
                onSuccess = {
                    setLoading(false)
                    Timber.d("List read all success")
                },
                onError = {
                    setLoading(false)
                    Timber.d("List read all failed =$it")
                }
            )
        }
    }

    fun fetchNotificationList() {
        setLoading(true)
        val param = hashMapOf<String?,String?>(
            "id_user" to user?.id,
            "status" to "all"
        )
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getNotification(
                param,
                onSuccess = {
                    setLoading(false)
                    notificationList.postValue(it)
                },
                onError = {
                    setLoading(false)
                    notificationList.postValue(listOf())
                }
            )
        }
    }


}
