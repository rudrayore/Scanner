package com.purnendu.scanner


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.launch
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.purnendu.scanner.scanner.ScannerActivityContract
import com.purnendu.scanner.ui.theme.ScannerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            ScannerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    val scannedData = remember { mutableStateOf("") }

                    //This Activity will open Scanner and after successful scanning get back to this activity with data
                    val scannerActivityLauncher =
                        rememberLauncherForActivityResult(contract = ScannerActivityContract())
                        { scannedResult ->
                            if (scannedResult != null) {
                                scannedData.value = scannedResult
                            }
                        }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            Button(onClick = {
                                scannerActivityLauncher.launch()
                            }) {
                                Text(text = "Scan")
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(text = scannedData.value)

                        }

                    }


                }

            }
        }


    }
}