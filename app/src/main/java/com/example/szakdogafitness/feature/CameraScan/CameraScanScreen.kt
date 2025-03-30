package com.example.szakdogafitness.feature.CameraScan

import android.graphics.Color
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.szakdogafitness.viewmodel.CameraEvent
import com.example.szakdogafitness.viewmodel.CameraViewModel
import kotlinx.coroutines.delay

@Composable
fun CameraScreen(
    cameraViewModel: CameraViewModel = hiltViewModel(),
    onNavBack: () -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember {
        LifecycleCameraController(context)
    }

    val cameraState by cameraViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        cameraViewModel.onEvent(CameraEvent.ResetState)
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            IconButton(
                onClick = {
                    cameraViewModel.onEvent(
                        CameraEvent.CapturePhoto(
                            context = context,
                            cameraController = cameraController
                        )
                    )
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = "takePhoto",
                    modifier = Modifier.size(50.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()){
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        setBackgroundColor(Color.BLACK)
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        previewView.controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                }
            )
            if (cameraState.image != null) {
                LaunchedEffect(cameraState.image) {
                    delay(200)
                    onNavBack()
                }
            }
        }
    }




}