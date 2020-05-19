package com.movies.sample.core.di

import android.content.Context
import androidx.room.Room
import com.movies.sample.AndroidApplication
import com.movies.sample.BuildConfig
import com.movies.sample.features.movies.repository.MoviesRepository
import com.movies.sample.features.movies.repository.MoviesRepositoryImpl
import com.movies.sample.features.movies.repository.db.AppDatabase
import com.movies.sample.features.movies.repository.db.MoviesDao
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: AndroidApplication) {

    @Provides @Singleton fun provideApplicationContext(): Context = application

    @Provides @Singleton fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/3383389/SampleData/master/Movies/")
                .client(createClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides @Singleton fun provideRoom(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "movies_db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides @Singleton fun provideMoviesDao(database: AppDatabase): MoviesDao {
        return database.moviesRepository()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }

    @Provides @Singleton fun provideMoviesRepository(repo: MoviesRepositoryImpl): MoviesRepository = repo
}
