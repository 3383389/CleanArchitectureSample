package com.movies.sample.core.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.movies.sample.BuildConfig
import com.movies.sample.core.exception.ErrorHandler
import com.movies.sample.features.movies.repository.MoviesRepository
import com.movies.sample.features.movies.repository.MoviesRepositoryImpl
import com.movies.sample.features.movies.repository.db.AppDatabase
import com.movies.sample.features.movies.repository.db.MoviesDao
import com.movies.sample.features.movies.repository.error.GeneralErrorHandlerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ApplicationModule.Declarations::class])
class ApplicationModule(private val application: Application) {

    @Module
    interface Declarations {

        @Binds
        @Singleton
        fun provideMoviesRepository(repo: MoviesRepositoryImpl): MoviesRepository

        @Binds
        @Singleton
        fun provideErrorHandler(handler: GeneralErrorHandlerImpl): ErrorHandler

    }

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/3383389/SampleData/master/Movies/")
            .client(createClient())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    @Provides
    @Singleton
    fun provideRoom(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "movies_db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesDao(database: AppDatabase): MoviesDao {
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
}
