package levkaantonov.com.study.tracker.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import levkaantonov.com.study.tracker.R
import levkaantonov.com.study.tracker.databinding.FragmentSetupBinding
import levkaantonov.com.study.tracker.other.Constants.KEY_FIRS_TIME_TOGGLE
import levkaantonov.com.study.tracker.other.Constants.KEY_NAME
import levkaantonov.com.study.tracker.other.Constants.KEY_WEIGHT
import levkaantonov.com.study.tracker.ui.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment() {
    private var _binding: FragmentSetupBinding? = null
    private val binding get() = _binding!!

    @set:Inject
    var isFirsAppOpen = true

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetupBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isFirsAppOpen) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()
            val action = SetupFragmentDirections.actionSetupFragmentToRunFragment()
            findNavController().navigate(
                action.actionId,
                savedInstanceState,
                navOptions
            )
        }

        binding.tvContinue.setOnClickListener {
            val success = writePersonalDataToSharedPrefs()
            if (success) {
                val action = SetupFragmentDirections.actionSetupFragmentToRunFragment()
                findNavController().navigate(action)
            } else {
                Snackbar.make(requireView(), "Please enter all the fields", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun writePersonalDataToSharedPrefs(): Boolean {
        binding.apply {
            val name = etName.text.toString()
            val weight = etWeight.text.toString()
            if (name.isEmpty() || weight.isEmpty()) {
                return false
            }
            sharedPrefs.edit()
                .putString(KEY_NAME, name)
                .putFloat(KEY_WEIGHT, weight.toFloat())
                .putBoolean(KEY_FIRS_TIME_TOGGLE, false)
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