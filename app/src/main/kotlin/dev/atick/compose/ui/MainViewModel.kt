package dev.atick.compose.ui

import android.content.Context
import android.os.Environment
import android.os.Looper
import androidx.core.os.HandlerCompat
import androidx.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import com.smartcare.oximetry.library.ConnectionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.atick.compose.data.OxiMeterData
import dev.atick.compose.utils.toCsv
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ThreadPoolExecutor
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val executor: ThreadPoolExecutor
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val recording = mutableListOf<OxiMeterData>()

    private val handler = HandlerCompat.createAsync(Looper.getMainLooper()) {
        val index = it.data.getInt(ConnectionManager.KEY_DATA_COUNTER)
        val timestamp = it.data.getLong(ConnectionManager.KEY_DATA_PACKAGE_RECEIVED)
        val deviceId = it.data.getInt(ConnectionManager.KEY_DATA_DEVICE_ID)
        val battery = it.data.getDouble(ConnectionManager.KEY_DATA_BATTERY)
        val ppgSignal = it.data.getInt(ConnectionManager.KEY_DATA_PLETH)
        val heartRate = it.data.getInt(ConnectionManager.KEY_DATA_PULSE)
        val spO2 = it.data.getDouble(ConnectionManager.KEY_DATA_SPO2)
        val redAdc = it.data.getLong(ConnectionManager.KEY_DATA_REDADC)
        val irAdc = it.data.getLong(ConnectionManager.KEY_DATA_IRADC)
        val spO2Status = it.data.getInt(ConnectionManager.KEY_DATA_SPO2STATUS)
        val perfusionIndex = it.data.getDouble(ConnectionManager.KEY_DATA_PERFUSIONINDEX)

        if (uiState.value.recordState == RecordState.Recording) {
            recording.add(
                OxiMeterData(
                    index, timestamp, deviceId, battery, ppgSignal,
                    heartRate, spO2, redAdc, irAdc, spO2Status, perfusionIndex
                )
            )
        }

        _uiState.value = uiState.value.copy(
            heartRate = heartRate
        )
        true
    }

    private val connectionManager = ConnectionManager(context, handler)


    fun connect() {
        if (uiState.value.connectionState == ConnectionState.Disconnected) {
            executor.execute {
                Logger.i("CONNECTING ... ")
                _uiState.value = uiState.value.copy(
                    connectionState = ConnectionState.Connecting
                )
                val connected = connectionManager.connect()
                if (connected) {
                    Logger.d("CONNECTED")
                    _uiState.value = uiState.value.copy(
                        connectionState = ConnectionState.Connected
                    )
                    connectionManager.startRecording()
                } else {
                    _uiState.value = uiState.value.copy(
                        connectionState = ConnectionState.Disconnected
                    )
                }
            }
        } else {
            executor.execute {
                Logger.i("DISCONNECTING ... ")
                _uiState.value = uiState.value.copy(
                    connectionState = ConnectionState.Disconnecting
                )
                connectionManager.disconnect()
                while (connectionManager.isConnected) Thread.sleep(100)
                Logger.d("DISCONNECTED")
                _uiState.value = uiState.value.copy(
                    connectionState = ConnectionState.Disconnected
                )
            }
        }
    }

    fun record() {
        if (uiState.value.recordState == RecordState.NotRecording) {
            Logger.d("STARTING TO RECORD ... ")
            _uiState.value = uiState.value.copy(
                recordState = RecordState.Recording
            )
        } else {
            Logger.d("SAVING DATA ... ")
            saveRecording()
            _uiState.value = uiState.value.copy(
                recordState = RecordState.NotRecording
            )
            recording.clear()
        }
    }

    private fun saveRecording() {
        val timestamp = SimpleDateFormat("dd_M_yyyy_hh_mm_ss", Locale.US).format(Date())
        val csvData = recording.toCsv()
        val myExternalFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "${timestamp}.csv"
        )

        try {
            val fos = FileOutputStream(myExternalFile)
            fos.write(csvData.toByteArray())
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}