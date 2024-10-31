package com.example.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab1.colorConversion.cmykToRgb
import com.example.lab1.colorConversion.hsvToRgb
import com.example.lab1.colorConversion.rgbToCmyk
import com.example.lab1.colorConversion.rgbToHsv
import com.example.lab1.models.Rgb
import com.example.lab1.ui.theme.Lab1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab1Theme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                  ColorPickerApp()
                }
            }
        }
    }
}

@Composable
fun ColorPickerApp() {
    // Состояния для RGB
    var red by remember { mutableStateOf(255) }
    var green by remember { mutableStateOf(0) }
    var blue by remember { mutableStateOf(0) }

    // Состояния для CMYK и HSV
    var cmyk = rgbToCmyk(red, green, blue)
    var hsv by remember { mutableStateOf(rgbToHsv(red, green, blue)) }

    // При изменении RGB пересчитываем остальные модели
    fun updateFromRgb() {
        cmyk = rgbToCmyk(red, green, blue)
        hsv = rgbToHsv(red, green, blue)
    }

    // При изменении CMYK пересчитываем RGB и HSV
    fun updateFromCmyk() {
        val rgb = cmykToRgb(cmyk.c, cmyk.m, cmyk.y, cmyk.k)
        red = rgb.r
        green = rgb.g
        blue = rgb.b
        hsv = rgbToHsv(red, green, blue)
    }

    // При изменении HSV пересчитываем RGB и CMYK
    fun updateFromHsv() {
        val rgb = hsvToRgb(hsv.h, hsv.s, hsv.v)
        red = rgb.r
        green = rgb.g
        blue = rgb.b
        cmyk = rgbToCmyk(red, green, blue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Слайдеры и поля для RGB
        Text(
            text = "RGB",
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp
        )
        ColorSliders("Red", red, 0, 255, onValueChange = {
            red = it
            updateFromRgb()
        })
        InputField("R", red, { red = it ?: 0; updateFromRgb() })

        ColorSliders("Green", green, 0, 255, onValueChange = {
            green = it
            updateFromRgb()
        })
        InputField("G", green, { if (it != null) {
            green = it
        }; updateFromRgb() })

        ColorSliders("Blue", blue, 0, 255, onValueChange = {
            blue = it
            updateFromRgb()
        })
        InputField("B", blue, { if (it != null) {
            blue = it
        }; updateFromRgb() })

        Spacer(modifier = Modifier.height(16.dp))

        // Слайдеры и поля для CMYK
        Text(
            text = "CMYK",
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp
        )
        ColorSliders("Cyan", (cmyk.c * 100).toInt(), 0, 100, onValueChange = {
            cmyk = cmyk.copy(c = it.toDouble() / 100)
            updateFromCmyk()
        })
        InputField("C", (cmyk.c * 100).toInt(), {
            cmyk = cmyk.copy(c = it!!.toDouble() / 100)
            updateFromCmyk()
        })

        ColorSliders("Magenta", (cmyk.m * 100).toInt(), 0, 100, onValueChange = {
            cmyk = cmyk.copy(m = it.toDouble() / 100)
            updateFromCmyk()
        })
        InputField("M", (cmyk.m * 100).toInt(), {
            cmyk = cmyk.copy(m = it!!.toDouble() / 100)
            updateFromCmyk()
        })

        ColorSliders("Yellow", (cmyk.y * 100).toInt(), 0, 100, onValueChange = {
            cmyk = cmyk.copy(y = it.toDouble() / 100)
            updateFromCmyk()
        })
        InputField("Y", (cmyk.y * 100).toInt(), {
            cmyk = cmyk.copy(y = it!!.toDouble() / 100)
            updateFromCmyk()
        })

        ColorSliders("Black", (cmyk.k * 100).toInt(), 0, 100, onValueChange = {
            cmyk = cmyk.copy(k = it.toDouble() / 100)
            updateFromCmyk()
        })
        InputField("K", (cmyk.k * 100).toInt(), {
            cmyk = cmyk.copy(k = it!!.toDouble() / 100)
            updateFromCmyk()
        })

        Spacer(modifier = Modifier.height(16.dp))

        // Слайдеры и поля для HSV
        Text(
            text = "HSV",
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp
        )
        ColorSliders("Hue", hsv.h.toInt(), 0, 360, onValueChange = {
            hsv = hsv.copy(h = it.toDouble())
            updateFromHsv()
        })
        InputField("H", hsv.h.toInt(), {
            hsv = hsv.copy(h = it!!.toDouble())
            updateFromHsv()
        })

        ColorSliders("Saturation", (hsv.s * 100).toInt(), 0, 100, onValueChange = {
            hsv = hsv.copy(s = it.toDouble() / 100)
            updateFromHsv()
        })
        InputField("S", (hsv.s * 100).toInt(), {
            hsv = hsv.copy(s = it!!.toDouble() / 100)
            updateFromHsv()
        })

        ColorSliders("Value", (hsv.v * 100).toInt(), 0, 100, onValueChange = {
            hsv = hsv.copy(v = it.toDouble() / 100)
            updateFromHsv()
        })
        InputField("V", (hsv.v * 100).toInt(), {
            hsv = hsv.copy(v = it!!.toDouble() / 100)
            updateFromHsv()
        })

        Spacer(modifier = Modifier.height(16.dp))

        // Отображение цветовых блоков для RGB, CMYK и HSV
        Text(text = "Отображение цветов")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ColorDisplayBox("RGB", Rgb(red, green, blue))
            ColorDisplayBox("CMYK", cmykToRgb(cmyk.c, cmyk.m, cmyk.y, cmyk.k))
            ColorDisplayBox("HSV", hsvToRgb(hsv.h, hsv.s, hsv.v))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Отображение текущих значений HSV
        Text(
            text = "Hue: %.2f°, Saturation: %.2f%%, Value: %.2f%%".format(
                hsv.h,
                hsv.s * 100,
                hsv.v * 100
            )
        )
    }
}


@Composable
fun InputField(label: String, value: Int?, onValueChange: (Int?) -> Unit) {
    var textValue by remember { mutableStateOf(value?.toString() ?: "0") }
    var isPlaceholderVisible by remember { mutableStateOf(value == null || value == 0) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$label:")
        Spacer(modifier = Modifier.width(8.dp))

        TextField(
            value = if (isPlaceholderVisible) "" else textValue,
            placeholder = { Text(text = "0") },
            onValueChange = { input ->
                val sanitizedInput = if (isPlaceholderVisible) input else input
                val intValue = sanitizedInput.toIntOrNull()

                onValueChange(intValue)

                textValue = sanitizedInput
                isPlaceholderVisible = sanitizedInput.isEmpty()
            },
            modifier = Modifier.width(80.dp)
        )
    }
}
@Composable
fun ColorDisplayBox(label: String, rgb: Rgb) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label)
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(rgbToComposeColor(rgb))  // Используем rgbToComposeColor для конвертации
        )
    }
}

// Ползунки для изменения цвета
@Composable
fun ColorSliders(label: String, value: Int, min: Int, max: Int, onValueChange: (Int) -> Unit) {
    Column {
        Text(text = label)
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = min.toFloat()..max.toFloat()
        )
        Text(text = value.toString())
    }
}

// Конвертация структуры Rgb в androidx.compose.ui.graphics.Color
fun rgbToComposeColor(rgb: Rgb): Color {
    return Color(rgb.r, rgb.g, rgb.b)
}
