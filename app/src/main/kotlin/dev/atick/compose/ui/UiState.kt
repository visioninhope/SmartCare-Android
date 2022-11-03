package dev.atick.compose.ui

sealed class ConnectionState(val description: String) {
    object Connecting : ConnectionState("CONNECTING ... ")
    object Connected : ConnectionState("DISCONNECT")
    object Disconnecting : ConnectionState("DISCONNECTING ... ")
    object Disconnected : ConnectionState("CONNECT")
}

sealed class RecordState(val description: String) {
    object Recording : RecordState("STOP RECORDING")
    object NotRecording : RecordState("RECORD")
}

data class UiState(
    val heartRate: Int = 0,
    val connectionState: ConnectionState = ConnectionState.Disconnected,
    val recordState: RecordState = RecordState.NotRecording
)