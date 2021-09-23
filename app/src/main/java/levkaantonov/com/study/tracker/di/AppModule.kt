package levkaantonov.com.study.tracker.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import levkaantonov.com.study.tracker.db.RunningDatabase
import levkaantonov.com.study.tracker.other.Constants.APP_SHARED_PREFS_NAME
import levkaantonov.com.study.tracker.other.Constants.DATABASE_NAME
import levkaantonov.com.study.tracker.other.Constants.KEY_FIRS_TIME_TOGGLE
import levkaantonov.com.study.tracker.other.Constants.KEY_NAME
import levkaantonov.com.study.tracker.other.Constants.KEY_WEIGHT
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app: Context
    ) =
        Room.databaseBuilder(
            app,
            RunningDatabase::class.java,
            DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideRunDao(db: RunningDatabase) = db.getRunDao()

    @Singleton
    @Provides
    fun provideSharedPrefences(
        @ApplicationContext app: Context
    ) = app.getSharedPreferences(APP_SHARED_PREFS_NAME, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideNameField(sharedPrefs: SharedPreferences) =
        sharedPrefs.getString(KEY_NAME, "")

    @Singleton
    @Provides
    fun provideWeightField(sharedPrefs: SharedPreferences) =
        sharedPrefs.getFloat(KEY_WEIGHT, 80f)

    @Singleton
    @Provides
    fun provideFirstTimeToggleField(sharedPrefs: SharedPreferences) =
        sharedPrefs.getBoolean(KEY_FIRS_TIME_TOGGLE, true)
}