package com.sostrovsky.travelup.ui.ticket

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.sostrovsky.travelup.R
import com.sostrovsky.travelup.ui.BaseActivity

class TicketActivity : BaseActivity(R.id.rootLayout) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setNavController()
    }

    private fun setNavController() {
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController,
            fragmentsWithHiddenUpButton())
    }

    private fun fragmentsWithHiddenUpButton(): AppBarConfiguration {
        return AppBarConfiguration.Builder(
                R.id.splashScreenFragment,
                R.id.searchTicketFragment
            ).build()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }
}
