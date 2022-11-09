package dev.atick.compose.di

import android.os.Looper
import androidx.core.os.HandlerCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object HandlerModule {

    @Provides
    @Singleton
    fun provideMainThreadHandler() = HandlerCompat
        .createAsync(Looper.getMainLooper())

}