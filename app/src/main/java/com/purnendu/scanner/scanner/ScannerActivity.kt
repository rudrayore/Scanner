package com.purnendu.scanner.scanner

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Size
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.*
import com.purnendu.scanner.ui.theme.ScannerTheme

class ScannerActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            //Camera Permission
            val permissionState = rememberPermissionState(
                permission = Manifest.permission.CAMERA
            )
            val context = LocalContext.current
            val lifeCycleOwner = LocalLifecycleOwner.current
            DisposableEffect(key1 = lifeCycleOwner)
            {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_START) {
                        permissionState.launchPermissionRequest()
                    }
                }
                lifeCycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifeCycleOwner.lifecycle.removeObserver(observer)
                }
            }
            ScannerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {

                    val cameraPermission = permissionState.status
                    //Checking permission status
                    when {
                        cameraPermission.isGranted -> {
                            ShowScanner(context)
                        }
                        cameraPermission.shouldShowRationale -> {
                            Toast.makeText(
                                context,
                                "Camera permission is needed to access camera",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        !cameraPermission.isGranted && !cameraPermission.shouldShowRationale -> {
                            Toast.makeText(
                                context,
                                "Camera permission permanently denied ,you can enable it by going to app setting",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


                }
            }
        }
    }

    //This will produce whole scanner UI
    @Composable
    private fun ShowScanner(context: Context) {
        val lifecycleOwner = LocalLifecycleOwner.current
        val cameraProviderFuture = remember {
            ProcessCameraProvider.getInstance(context)
        }
        Box(modifier = Modifier.fillMaxSize()) {

            //This is for Camerax for rendering preview
            AndroidView(
                factory = { context ->
                    val previewView = PreviewView(context)
                    val preview = Preview.Builder().build()
                    val selector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()
                    preview.setSurfaceProvider(previewView.surfaceProvider)
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setTargetResolution(
                            Size(
                                previewView.width,
                                previewView.height
                            )
                        )
                        .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                    imageAnalysis.setAnalyzer(
                        ContextCompat.getMainExecutor(context),
                        QrCodeAnalyzer { result ->
                            //After getting the scanned data, send this result to the calling activity and then finish this activity
                            val intent = Intent().putExtra(
                                ScannerActivityContract.REQUEST_KEY,
                                result
                            )
                            setResult(RESULT_OK, intent)
                            finish()
                        })

                    try {
                        cameraProviderFuture.get().bindToLifecycle(
                            lifecycleOwner,
                            selector,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    previewView
                },
                modifier = Modifier
                    .fillMaxSize()
            )

            //This will create blur layout except for focused area
            TransparentScannerLayout(
                modifier = Modifier.fillMaxSize(),
                width = 200.dp,
                height = 200.dp,
                offsetY = 150.dp
            )

        }

    }
}
