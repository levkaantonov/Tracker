package levkaantonov.com.study.tracker.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import levkaantonov.com.study.tracker.R
import levkaantonov.com.study.tracker.databinding.ActivityMainBinding
import levkaantonov.com.study.tracker.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import levkaantonov.com.study.tracker.ui.fragments.TrackingFragmentDirections

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigateToTrackingFragmentIfNeeded(intent)

        setSupportActionBar(binding.toolbar)
        val navHost =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHost.findNavController()
        navController?.let {
            binding.bottomNavigationView.setupWithNavController(it)
            binding.bottomNavigationView.setOnItemReselectedListener { }
            it.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.settingsFragment, R.id.runFragment, R.id.statisticsFragment ->
                        binding.bottomNavigationView.visibility = View.VISIBLE
                    else -> binding.bottomNavigationView.visibility = View.GONE
                }
            }
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {
            val action = TrackingFragmentDirections.actionGlobalTrackingFragment()
            navController?.navigate(action)
        }
    }

    fun setToolbarText(text: String) {
        binding.tvToolbarTitle.text = text
    }
}