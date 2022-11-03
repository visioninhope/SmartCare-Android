package dev.atick.compose.data

data class OxiMeterData(
    val index: Int,
    val timestamp: Long,
    val deviceId: Int,
    val battery: Double,
    val ppgSignal: Int,
    val heartRate: Int,
    val spO2: Double,
    val redAdc: Long,
    val irAdc: Long,
    val spO2Status: Int,
    val perfusionIndex: Double
) {
    override fun toString(): String {
        return "$index,$timestamp,$deviceId,$battery,$ppgSignal,$heartRate," +
            "$spO2,$redAdc,$irAdc,$spO2Status,$perfusionIndex"
    }
}


/*

KEY_DATA_PACKAGE_RECEIVED: The UNIX timestamp (milliseconds since 1970-01-01) when the data package was received (long).
Note that due to buffering in the oximeter and latency in the Bluetooth connection, this may not accurately reflect the timestamp when the data package was sampled.
KEY_DATA_PLETH: The latest value of plethysmography signal (int).
KEY_DATA_PULSE: The latest value of heart rate (int).
KEY_DATA_SPO2: The latest value of oxygen saturation (double).
KEY_DATA_DEVICE_ID: The ID of the pulse oximeter (int).
KEY_DATA_REDADC: The latest value from the red light sensor ADC (long).
KEY_DATA_IRADC: The latest value from the Infrared light sensor ADC (long).
KEY_DATA_BATTERY: The latest battery level in mV (double).
KEY_DATA_COUNTER: The count of the current data packet (int).
KEY_DATA_SPO2STATUS: The current status of the pulse oximeter (int).
KEY_DATA_PERFUSIONINDEX: The latest blood perfusion index in % (double).
 */