package com.example.lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lab2.ui.theme.Lab2Theme
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color as ComposeColor



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ImageProcessingApp()
                }
            }
        }
    }
}

@Composable
fun ImageProcessingApp() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var originalBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var processedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    // Лаунчер для выбора изображения из галереи
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                imageUri = uri
                // Конвертация Uri в Bitmap
                originalBitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }
                processedBitmap = originalBitmap // Устанавливаем оригинальное изображение
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        // Кнопка для выбора изображения из галереи
        Button(onClick = {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            launcher.launch(intent)
        }) {
            Text("Загрузить изображение из галереи")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка для применения эквализации гистограммы
        Button(
            onClick = {
                originalBitmap?.let {
                    processedBitmap = histogramEqualization(it)
                }
            },
            enabled = originalBitmap != null
        ) {
            Text("Применить эквализацию гистограммы")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Отображение оригинального и обработанного изображений
        Row {
            originalBitmap?.let {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Оригинал", color = ComposeColor.Gray)
                    Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier.size(200.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            processedBitmap?.let {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Обработанное", color = ComposeColor.Gray)
                    Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier.size(200.dp))
                }
            }
        }
    }
}