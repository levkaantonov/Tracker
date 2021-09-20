package levkaantonov.com.study.tracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import levkaantonov.com.study.tracker.repositories.MainRepository
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
}