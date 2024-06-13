package com.brandy.codes

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


/*
* This composable function handles permission request
* */
@Composable
fun RequestPermission(
    permission: String,
    rationale: String = "",
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    var showRationaleDialog by remember { mutableStateOf(false) }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                onPermissionGranted()
            } else {
                if (shouldShowRequestPermissionRationale(context, permission)) {
                    showRationaleDialog = true
                } else {
                    onPermissionDenied()
                }
            }
        }
    )

    // Check permission status
    val permissionStatus = ContextCompat.checkSelfPermission(
        context, permission
    )

    if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
        onPermissionGranted()
    } else {
        // Request permission
        LaunchedEffect(Unit) {
            permissionLauncher.launch(permission)
        }
    }

    // Show rationale dialog if needed
    if (showRationaleDialog) {
        AlertDialog(
            onDismissRequest = { showRationaleDialog = false },
            title = { Text(text = "Permission Required") },
            text = { Text(text = rationale) },
            confirmButton = {
                TextButton(onClick = {
                    showRationaleDialog = false
                    permissionLauncher.launch(permission)
                }) {
                    Text(text = "Retry")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRationaleDialog = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}

fun shouldShowRequestPermissionRationale(context: Context, permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)
}

