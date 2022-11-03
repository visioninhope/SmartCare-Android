package dev.atick.compose.ui

import android.animation.ValueAnimator
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.LottieAnimationView
import dev.atick.compose.R

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = viewModel()
) {

    val uiState by mainViewModel.uiState.collectAsState()

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Heart Rate",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.onBackground
        )
        Text(
            text = "98.0",
            fontSize = 80.sp,
            fontWeight = FontWeight.Thin,
            color = MaterialTheme.colors.onBackground
        )
        Text(
            text = "BPM",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.onBackground
        )

        AndroidView(
            factory = { ctx ->
                LottieAnimationView(ctx).apply {
                    setAnimation(R.raw.heart_beat)
                    repeatCount = ValueAnimator.INFINITE
                    speed = 2.0F
                    playAnimation()
                }
            },
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth(0.4F)
        )

        Button(
            modifier = Modifier
                .fillMaxWidth(0.5F)
                .height(40.dp),
            shape = RoundedCornerShape(20.dp),
            onClick = { /*TODO*/ }
        ) {
            Text(text = uiState.connectionState.description)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth(0.5F)
                .height(40.dp),
            shape = RoundedCornerShape(20.dp),
            enabled = uiState.connectionState == ConnectionState.Connected,
            onClick = { /*TODO*/ }
        ) {
            Text(text = uiState.recordState.description)
        }
    }
}