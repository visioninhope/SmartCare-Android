package dev.atick.compose.di

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.smartcare.oximetry.library.ConnectionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module(
    includes = [
        HandlerModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object SmartCareModule {

    @Provides
    @Singleton
    fun provideConnectionManager(
        @ApplicationContext context: Context,
        mainThreadHandler: Handler
    ) = ConnectionManager(context, mainThreadHandler)

}