package dev.atick.compose.di

import android.bluetooth.BluetoothAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.compose.utils.PermissionUtils
import javax.inject.Singleton

@Module(
    includes = [
        BluetoothAdapterModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object PermissionUtilsModule {

    @Provides
    @Singleton
    fun providePermissionUtils(
        bluetoothAdapter: BluetoothAdapter?
    ): PermissionUtils {
        return PermissionUtils(bluetoothAdapter)
    }
}