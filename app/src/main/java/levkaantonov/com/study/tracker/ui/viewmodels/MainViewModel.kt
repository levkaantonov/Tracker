package levkaantonov.com.study.tracker.ui.viewmodels

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import levkaantonov.com.study.tracker.db.Run
import levkaantonov.com.study.tracker.repositories.MainRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val _sortType = MutableLiveData<SortType>()
    val sortType: LiveData<SortType> = _sortType

    val runs: LiveData<List<Run>> = Transformations.switchMap(_sortType) { type ->
        when (type) {
            SortType.DATE -> {
                mainRepository.getAllRunsSortedByDate()
            }
            SortType.RUNNING_TIME -> {
                mainRepository.getAllRunsSortedByTimeInMillis()
            }
            SortType.AVG_SPEED -> {
                mainRepository.getAllRunsSortedByAvgSpeedInKMH()
            }
            SortType.DISTANCE -> {
                mainRepository.getAllRunsSortedByDistanceInMeters()
            }
            SortType.CALORIES_BURNED -> {
                mainRepository.getAllRunsSortedByCaloriesBurned()
            }
        }
    }

    init {
        _sortType.value = SortType.DATE
    }

    fun insertRun(run: Run) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }

    fun changeSortType(type: SortType) {
        _sortType.value = type
    }
}