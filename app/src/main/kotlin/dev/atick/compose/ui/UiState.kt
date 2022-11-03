package dev.atick.compose.ui

sealed class ConnectionState(val description: String) {
    object Disconnected : ConnectionState("CONNECT")
    object Connecting : ConnectionState("CONNECTING ... ")
    object Connected : ConnectionState("CONNECTED")
}

sealed class RecordState(val description: String) {
    object Recording : RecordState("RECORDING ... ")
    object NotRecording : RecordState("RECORD")
}

data class UiState(
    val connectionState: ConnectionState = ConnectionState.Disconnected,
    val recordState: RecordState = RecordState.NotRecording
)