package levkaantonov.com.study.tracker.repositories

import levkaantonov.com.study.tracker.db.Run
import levkaantonov.com.study.tracker.db.RunDao
import javax.inject.Inject

class MainRepository @Inject constructor(private val runDao: RunDao) {

    suspend fun insertRun(run: Run) = runDao.insertRun(run)

    suspend fun deleteRun(run: Run) = runDao.deleteRun(run)

    fun getAllRunsSortedByDate() = runDao.getAllRunsSortedByDate()

    fun getAllRunsSortedByTimeInMillis() = runDao.getAllRunsSortedByTimeInMillis()

    fun getAllRunsSortedByCaloriesBurned() = runDao.getAllRunsSortedByCaloriesBurned()

    fun getAllRunsSortedByAvgSpeedInKMH() = runDao.getAllRunsSortedByAvgSpeedInKMH()

    fun getAllRunsSortedByDistanceInMeters() = runDao.getAllRunsSortedByDistanceInMeters()

    fun getTotalTimeInMillis() = runDao.getTotalTimeInMillis()

    fun getTotalCaloriesBurned() = runDao.getTotalCaloriesBurned()

    fun getTotalDistanceInMeters() = runDao.getTotalDistanceInMeters()

    fun getTotalAvgSpeed() = runDao.getTotalAvgSpeed()
}

