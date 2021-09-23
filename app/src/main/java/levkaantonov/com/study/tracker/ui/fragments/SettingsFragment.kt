package levkaantonov.com.study.tracker.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import levkaantonov.com.study.tracker.R
import levkaantonov.com.study.tracker.databinding.FragmentSettingsBinding
import levkaantonov.com.study.tracker.other.Constants
import levkaantonov.com.study.tracker.ui.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    @Inject
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFieldsFromSharedPrefs()
        binding.apply {
            btnApplyChanges.setOnClickListener {
                val success = applyChangesToSharedPrefs()
                if (success) {
                    Snackbar.make(view, "Saved changes", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(view, "Please fill all the fields", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun loadFieldsFromSharedPrefs() {
        val weight = sharedPrefs.getFloat(Constants.KEY_WEIGHT, 80f)
        val name = sharedPrefs.getString(Constants.KEY_NAME, "")
        binding.apply {
            etName.setText(name.toString())
            etWeight.setText(weight.toString())
        }

    }

    private fun applyChangesToSharedPrefs(): Boolean {
        binding.apply {
            val name = etName.text.toString()
            val weight = etWeight.text.toString()

            if (name.isEmpty() || weight.isEmpty()) {
                return false
            }

            sharedPrefs.edit()
                .putString(Constants.KEY_NAME, name)
                .putFloat(Constants.KEY_WEIGHT, weight.toFloat())
                .putBoolean(Constants.KEY_FIRS_TIME_TOGGLE, false)
                .apply()
            val toolBarText = "Let's go, ${name}!"
            (requireActivity() as MainActivity).setToolbarText(toolBarText)
            return true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}