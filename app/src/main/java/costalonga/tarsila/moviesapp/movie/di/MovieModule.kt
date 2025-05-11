package costalonga.tarsila.moviesapp.movie.di

import android.content.Context
import androidx.room.Room
import costalonga.tarsila.moviesapp.movie.data.MovieRepositoryImpl
import costalonga.tarsila.moviesapp.movie.data.local.MovieDatabase
import costalonga.tarsila.moviesapp.movie.data.remote.api.MovieApi
import costalonga.tarsila.moviesapp.movie.data.remote.api.MovieApiInstance
import costalonga.tarsila.moviesapp.movie.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieModule {

    @Provides
    @Singleton
    fun provideMovieApi(): MovieApi {
        return MovieApiInstance.createService(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movies_db"
        ).build()
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideMovieRepository(repository: MovieRepositoryImpl): MovieRepository

}