package levkaantonov.com.study.tracker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import levkaantonov.com.study.tracker.db.RunningDatabase
import levkaantonov.com.study.tracker.other.Constants.DATABASE_NAME
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
}