package dev.atick.compose.utils

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.orhanobut.logger.Logger
import dev.atick.core.utils.extensions.hasPermission
import dev.atick.core.utils.extensions.permissionLauncher
import dev.atick.core.utils.extensions.resultLauncher
import javax.inject.Inject

class PermissionUtils @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter?
) {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var bleLauncher: ActivityResultLauncher<Intent>

    fun initialize(
        activity: ComponentActivity,
        onSuccess: () -> Unit
    ) {
        permissionLauncher = activity.permissionLauncher(
            onSuccess = {
                Logger.i("ALL PERMISSIONS GRANTED")
                enableBluetooth()
            },
            onFailure = {
                Logger.d("Permission Not GRANTED")
                activity.finishAffinity() }
        )

        bleLauncher = activity.resultLauncher(
            onSuccess = {
                Logger.i("BLUETOOTH IS ENABLED")
                onSuccess.invoke()
            },
            onFailure = { activity.finishAffinity() }
        )
    }

    private fun isAllPermissionsProvided(activity: ComponentActivity): Boolean {
        return isBluetoothPermissionGranted(activity) &&
            isLocationPermissionGranted(activity) &&
            isStoragePermissionGranted(activity) &&
            bluetoothAdapter?.isEnabled ?: false
    }

    fun setupBluetooth(activity: ComponentActivity) {
        if (bluetoothAdapter == null) activity.finishAffinity()
        showPermissionRationale(activity)
    }

    private fun enableBluetooth() {
        val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        bleLauncher.launch(enableIntent)
    }

    private fun askForPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            permissions.addAll(
                 listOf(
                     Manifest.permission.READ_EXTERNAL_STORAGE,
                     Manifest.permission.WRITE_EXTERNAL_STORAGE,
                 )
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.addAll(
                listOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        permissionLauncher.launch(permissions.toTypedArray())
    }

    private fun showPermissionRationale(activity: ComponentActivity) {
        if (!isAllPermissionsProvided(activity)) {
            askForPermissions()
            /*
            activity.showAlertDialog(
                title = "Permission Required",
                message = "This app requires Bluetooth connection " +
                    "to work properly. Please provide Bluetooth permission. " +
                    "Scanning for BLE devices also requires Location Access " +
                    "Permission. However, location information will NOT be" +
                    "used for tracking.",
                onApprove = {
                    Logger.i("Permission Rationale Approved")
                    askForPermissions()
                },
                onCancel = { activity.finishAffinity() }
            )
             */
        }
    }

    private fun isBluetoothPermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.hasPermission(Manifest.permission.BLUETOOTH_SCAN) &&
                context.hasPermission(Manifest.permission.BLUETOOTH_CONNECT)
        } else true
    }

    private fun isLocationPermissionGranted(context: Context): Boolean {
        return context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun isStoragePermissionGranted(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) return true
        return context.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE) &&
            context.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}
