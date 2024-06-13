import android.Manifest
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.brandy.codes.CameraPreview
import com.brandy.codes.RequestPermissionWithSettingsDialog

@Composable
fun MyApp() {
    var showContent by remember { mutableStateOf(false) }
    var requestPermission by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    if (requestPermission) {
        RequestPermissionWithSettingsDialog(
            permission = Manifest.permission.CAMERA,
            rationale = "This app needs camera access to take photos.",
            onPermissionGranted = {
                showContent = true
            }
        )
    }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showContent) {
                    CameraPreview(
                        modifier = Modifier.weight(1f),
                        onImageCaptured = { uri ->
                            imageUri = uri
                        }
                    )
                } else {
                    Text(
                        text = "Camera permission not granted",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(onClick = { requestPermission = true }) {
                        Text(text = "Request Camera Permission")
                    }
                }

                imageUri?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        bitmap = MediaStore.Images.Media.getBitmap(LocalContext.current.contentResolver, it).asImageBitmap(),
                        contentDescription = "Captured Image",
                        modifier = Modifier.size(300.dp)
                    )
                }
            }
        }
    }
}

