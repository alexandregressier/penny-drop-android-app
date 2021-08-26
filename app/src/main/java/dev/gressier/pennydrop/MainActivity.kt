package dev.gressier.pennydrop

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        // Theme
        setTheme(when (prefs.getString("theme", "AppTheme")) {
            "Crew" -> R.style.Crew
            "FTD" -> R.style.FTD
            "GPG" -> R.style.GPG
            "Hazel" -> R.style.Hazel
            "Kotlin" -> R.style.Kotlin
            else -> R.style.AppTheme
        } )
        // Theme Mode
        setDefaultNightMode(when (prefs.getString("themeMode", "")) {
            "Light" -> MODE_NIGHT_NO
            "Dark" -> MODE_NIGHT_YES
            else -> MODE_NIGHT_FOLLOW_SYSTEM
        })
        // Layout
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Navigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.containerFragment) as NavHostFragment
        navController = navHostFragment.navController

        findViewById<BottomNavigationView>(R.id.bottom_nav).let { bottomNav ->
            bottomNav.setupWithNavController(navController)
            setupActionBarWithNavController(navController, AppBarConfiguration(bottomNav.menu))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
        true.also {
            super.onCreateOptionsMenu(menu)
            menuInflater.inflate(R.menu.options, menu)
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        if (::navController.isInitialized)
            item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        else false

    override fun onSupportNavigateUp(): Boolean =
        ::navController.isInitialized && navController.navigateUp() || super.onSupportNavigateUp()
}