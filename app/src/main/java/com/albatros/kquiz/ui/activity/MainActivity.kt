package com.albatros.kquiz.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.albatros.kquiz.R
import com.albatros.kquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onUserInteraction() {
        super.onUserInteraction()
        setWindowState()
    }

    fun setBackground(id: Int) {
        binding.root.background = ResourcesCompat.getDrawable(resources, id, theme)
    }

    fun setDefaultColor() {
        binding.root.background = ResourcesCompat.getDrawable(resources, R.drawable.main_grad, theme)
    }

    @Suppress("deprecation")
    private fun setWindowState() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val type = WindowInsets.Type.systemBars()
            window.insetsController?.hide(type)
        } else {
            var flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            flag = flag or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            flag = flag or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            flag = flag or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            flag = flag or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            window.decorView.systemUiVisibility = flag
        }
    }

    interface IOnBackPressed {
        fun onBackPressed(): Boolean
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val res = fragment.childFragmentManager.fragments.lastOrNull()
        if (res is IOnBackPressed) res.onBackPressed() else super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setWindowState()

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}