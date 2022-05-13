package com.telemedicine.indihealth.http

import com.telemedicine.indihealth.network.service.assesment.DoctorAssesmentPatientService
import com.telemedicine.indihealth.network.service.consultation.ConsultationService
import com.telemedicine.indihealth.network.service.doctor.ConsultationDoctorService
import com.telemedicine.indihealth.network.service.forgotpassword.ForgotPasswordService
import com.telemedicine.indihealth.network.service.history.HistoryPaymentService
import com.telemedicine.indihealth.network.service.historylog.DoctorHistoryLogService
import com.telemedicine.indihealth.network.service.login.LoginService
import com.telemedicine.indihealth.network.service.main.MainService
import com.telemedicine.indihealth.network.service.pharmacy.PharmacyService
import com.telemedicine.indihealth.network.service.profile.ProfileService
import com.telemedicine.indihealth.network.service.queue.QueueService
import com.telemedicine.indihealth.network.service.recipe.RecipeService
import com.telemedicine.indihealth.network.service.records.MedicalRecordsService
import com.telemedicine.indihealth.network.service.registration.RegistrationService
import com.telemedicine.indihealth.network.service.schedule.DoctorScheduleService
import com.telemedicine.indihealth.network.service.toc.TocService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideTocService(retrofit: Retrofit): TocService {
        return retrofit.create(TocService::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }
    @Provides
    @Singleton
    fun provideRegistrationService(retrofit: Retrofit): RegistrationService {
        return retrofit.create(RegistrationService::class.java)
    }
    @Provides
    @Singleton
    fun provideForgotPasswordService(retrofit: Retrofit): ForgotPasswordService {
        return retrofit.create(ForgotPasswordService::class.java)
    }
    @Provides
    @Singleton
    fun provideMainService(retrofit: Retrofit): MainService {
        return retrofit.create(MainService::class.java)
    }
    @Provides
    @Singleton
    fun provideConsultationService(retrofit: Retrofit): ConsultationService {
        return retrofit.create(ConsultationService::class.java)
    }
    @Provides
    @Singleton
    fun provideConsultationDoctorService(retrofit: Retrofit): ConsultationDoctorService {
        return retrofit.create(ConsultationDoctorService::class.java)
    }
    @Provides
    @Singleton
    fun provideProfileService(retrofit: Retrofit): ProfileService {
        return retrofit.create(ProfileService::class.java)
    }
    @Provides
    @Singleton
    fun provideQueueService(retrofit: Retrofit): QueueService {
        return retrofit.create(QueueService::class.java)
    }
    @Provides
    @Singleton
    fun provideHistoryPaymentService(retrofit: Retrofit): HistoryPaymentService {
        return retrofit.create(HistoryPaymentService::class.java)
    }
    @Provides
    @Singleton
    fun provideRecipeService(retrofit: Retrofit): RecipeService {
        return retrofit.create(RecipeService::class.java)
    }
    @Provides
    @Singleton
    fun provideMedicalRecordsService(retrofit: Retrofit): MedicalRecordsService {
        return retrofit.create(MedicalRecordsService::class.java)
    }
    @Provides
    @Singleton
    fun provideDoctorScheduleService(retrofit: Retrofit): DoctorScheduleService {
        return retrofit.create(DoctorScheduleService::class.java)
    }
    @Provides
    @Singleton
    fun provideDoctorAssesmentPatientService(retrofit: Retrofit): DoctorAssesmentPatientService {
        return retrofit.create(DoctorAssesmentPatientService::class.java)
    }
    @Provides
    @Singleton
    fun provideDoctorHistoryLogService(retrofit: Retrofit): DoctorHistoryLogService {
        return retrofit.create(DoctorHistoryLogService::class.java)
    }
    @Provides
    @Singleton
    fun providePharmacyService(retrofit: Retrofit): PharmacyService {
        return retrofit.create(PharmacyService::class.java)
    }
}