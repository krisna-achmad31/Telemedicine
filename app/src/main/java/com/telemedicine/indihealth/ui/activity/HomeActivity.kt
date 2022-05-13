package com.telemedicine.indihealth.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.telemedicine.indihealth.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*


@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    var currentDestinationId = R.id.mainFragment

    var navHostFragment : NavHostFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setUpNavigation()
    }

    private fun setUpNavigation() {
        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        NavigationUI.setupWithNavController(
            bottomNavigation,
            navHostFragment!!.navController
        )
        navHostFragment?.navController
            ?.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainFragment -> showBottomNav()
                R.id.historyPaymentFragment -> showBottomNav()
                R.id.profileShowFragment -> showBottomNav()
                R.id.doctorContainerFragment -> showBottomNav()
                R.id.notificationFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }

        bottomNavigation.setOnNavigationItemSelectedListener(this)
    }
    private fun showBottomNav() {
        bottomNavigation.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        bottomNavigation.visibility = View.GONE

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val sameDestination = item.itemId == currentDestinationId
        if(!sameDestination){
            navHostFragment?.navController?.navigate(item.itemId)
            currentDestinationId = item.itemId
        }
        return !sameDestination
    }
}