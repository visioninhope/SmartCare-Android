package dev.atick.compose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.compose.R
import dev.atick.compose.utils.PermissionUtils
import dev.atick.core.ui.theme.ComposeTheme
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var permissionUtils: PermissionUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_JetpackComposeStarter)
        setContent {
            ComposeTheme {
                MainScreen()
            }
        }

        permissionUtils.initialize(this) {
            Logger.i("ALL PERMISSION GRANTED!")
        }

//        Thread {
//            Runnable {
//                Logger.d("CONNECTING ... ")
//            }
//        }.start()

//        thread {
//                Logger.d("CONNECTING ... ")
//
//        }

//        val myExternalFile = File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
//            "data.csv"
//        )
//
//        Logger.i(myExternalFile.absolutePath)
//
//        try {
//            val fos = FileOutputStream(myExternalFile)
//            fos.write("Hello".toByteArray())
//            fos.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
    }

    override fun onResume() {
        permissionUtils.setupBluetooth(this)
        super.onResume()
    }

//    private fun isExternalStorageReadOnly(): Boolean {
//        val extStorageState = Environment.getExternalStorageState()
//        return Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState
//    }
//
//    private fun isExternalStorageAvailable(): Boolean {
//        val extStorageState = Environment.getExternalStorageState()
//        return Environment.MEDIA_MOUNTED == extStorageState
//    }
}