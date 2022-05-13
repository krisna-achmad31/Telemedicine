package com.telemedicine.indihealth.ui.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.helper.AppPermission
import com.telemedicine.indihealth.helper.AppToast
import com.telemedicine.indihealth.ui.fragment.main.MainFragment
import com.telemedicine.indihealth.ui.fragment.splashscreen.SplashScreenFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.getStringExtra("toOpen") == "assesment"){
            Timber.d("ADA EXTRA ASSESMENT")
//            val navController = findNavController(R.id.navigation_main)
//            navController.navigate(R.id.action_mainFragment_to_historyPaymentFragment)
            val bundle = Bundle()
            bundle.putString("toOpen", "assesment")
            val fragobj = SplashScreenFragment()
            fragobj.setArguments(bundle)
        }

        AppPermission.apply {
            onAllPermissionGranted = {
            }
            onAllPermissionPermanentlyDenied = {
                showSettingsDialog()
            }
            onPermissionDenied = {
                AppToast.set(
                    this@MainActivity,
                    "Harus mengijinkan semua permission terlebih dahulu"
                )
                showSettingsDialog()
            }

            permission(this@MainActivity)
//
//            val navController = findNavController(R.id.navigation_main)
//            navController.navigate(R.id.action_mainFragment_to_historyPaymentFragment)
//
//            startActivity(Intent(this, HomeActivity.class))
        }

        val bundle = Bundle()
        bundle.putString("toOpen", "ASUUUUUUUUUUUUU")
        val fragobj = MainFragment()
        fragobj.setArguments(bundle)

        checkBatteryOptimization(this)
        checkCanDrawOverlays()

//        if (checkDeviceRoot())
//            return
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle("Butuh Perizinan")
        builder.setMessage("Aplikasi ini memerlukan izin untuk menggunakan fitur ini. Anda dapat memberikannya di setelan aplikasi.")
        builder.setPositiveButton("Ke Setelan") { dialog: DialogInterface, which: Int ->
            dialog.cancel()
            finish()
            openSettings()
        }
        builder.setNegativeButton(
            "Tutup Aplikasi"
        ) { dialog: DialogInterface, which: Int ->
            dialog.cancel()
            finish()
        }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun checkBatteryOptimization(context: Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val pm:PowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            if(!pm.isIgnoringBatteryOptimizations(context.packageName)){
                val builder = AlertDialog.Builder(this)
                builder.setCancelable(false)
                builder.setTitle("Matikan Optimalisasi Baterai")
                builder.setMessage("Matikan setelan di pengaturan untuk menunjang kinerja aplikasi ini")
                builder.setPositiveButton("Ke Setelan") { dialog: DialogInterface, which: Int ->
                    dialog.cancel()
                    val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                    startActivityForResult(intent, 1)
                }
                builder.setNegativeButton(
                    "Biarkan"
                ) { dialog: DialogInterface, which: Int ->
                    dialog.cancel()
                }
                builder.show()
            }
        }
    }

    private fun checkCanDrawOverlays(){
        if (!Settings.canDrawOverlays(this)) {
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            builder.setTitle("Butuh Perizinan")
            builder.setMessage("Izinkan Aplikasi berjalan diatas aplikasi lain")
            builder.setPositiveButton("Ke Setelan") { dialog: DialogInterface, which: Int ->
                dialog.cancel()
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, 2)
            }
            builder.setNegativeButton(
                "Keluar Aplikasi"
            ) { dialog: DialogInterface, which: Int ->
                dialog.cancel()
                finish()
            }
            builder.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            checkBatteryOptimization(this)
        }
        if (requestCode == 2){
            checkCanDrawOverlays()
        }
    }
}