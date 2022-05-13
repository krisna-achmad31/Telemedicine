package com.telemedicine.indihealth.http

import android.annotation.SuppressLint
import android.os.Build
import com.skydoves.sandwich.coroutines.CoroutinesDataSourceCallAdapterFactory
import com.squareup.moshi.Moshi
import com.telemedicine.indihealth.helper.AppVar
import com.telemedicine.indihealth.network.HttpRequestInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule  {

    private val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
        object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
    )


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)
//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP ||
//            Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            return OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .addInterceptor(HttpRequestInterceptor())
                .addInterceptor(logger)
                .hostnameVerifier { hostname, session -> true }
                .build()
//        }else{
//            return OkHttpClient.Builder()
//                .connectTimeout(3, TimeUnit.MINUTES)
//                .readTimeout(3, TimeUnit.MINUTES)
//                .writeTimeout(3, TimeUnit.MINUTES)
//                .addInterceptor(HttpRequestInterceptor())
//                .build()
//        }

    }


    /*
    @Provides
    @Singleton
    fun provideUntrustedOkHttpClient(): OkHttpClient{
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) = Unit

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) = Unit

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()

                    }

                }
            )

            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
     */

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(AppVar.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutinesDataSourceCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi{
        return Moshi.Builder().build()
    }
}