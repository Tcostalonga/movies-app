package costalonga.tarsila.moviesapp.movie.di

import costalonga.tarsila.moviesapp.movie.data.MovieRepositoryImpl
import costalonga.tarsila.moviesapp.movie.data.api.MovieApi
import costalonga.tarsila.moviesapp.movie.data.api.MovieApiInstance
import costalonga.tarsila.moviesapp.movie.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
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
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideMovieRepository(repository: MovieRepositoryImpl): MovieRepository

}