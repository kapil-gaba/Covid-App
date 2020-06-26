package com.example.covidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.os.postDelayed
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    lateinit var navController: NavController
    private var backPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
    }

    fun setupViews() {
        var navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragNavHost) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavView, navHostFragment.navController)

        //var appBarConfiguration = AppBarConfiguration(navHostFragment.navController.graph)
        var appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.globalFragment,
                R.id.countriesFragment,
                R.id.aboutFragment
            )
        )
        setupActionBarWithNavController(navHostFragment.navController, appBarConfiguration)
    }

    override fun onBackPressed() {
        if (navController.graph.startDestination == navController.currentDestination?.id) {
            if (backPressedOnce) {
                super.onBackPressed()
                return
            }

            backPressedOnce = true
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show()

            Handler().postDelayed(2000) {
                backPressedOnce = false
            }
        } else {
            super.onBackPressed()
        }
    }
}
