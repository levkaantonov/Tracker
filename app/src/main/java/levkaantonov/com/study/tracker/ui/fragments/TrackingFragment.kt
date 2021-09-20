package levkaantonov.com.study.tracker.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import levkaantonov.com.study.tracker.R
import levkaantonov.com.study.tracker.ui.viewmodels.MainViewModel

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private val viewModel: MainViewModel by viewModels()
}