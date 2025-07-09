package com.multrm.weather.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.multrm.weather.R
import com.multrm.data.db.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DbModule {
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(context.getString(R.string.app_name), MODE_PRIVATE)
    }

    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context, WeatherDatabase::class.java, "Weather Database"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideNtDao(weatherDatabase: WeatherDatabase) = weatherDatabase.weatherDao()
}