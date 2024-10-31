package com.example.lab2

import android.graphics.Bitmap
import android.graphics.Color

fun histogramEqualization(bitmap: Bitmap): Bitmap {
    val result = bitmap.copy(bitmap.config, true)

    // Шаг 1: Вычисление гистограммы
    val histogram = IntArray(256) { 0 }
    for (y in 0 until bitmap.height) {
        for (x in 0 until bitmap.width) {
            val color = bitmap.getPixel(x, y)
            val brightness = Color.red(color) // Работаем с изображением в градациях серого
            histogram[brightness]++
        }
    }

    // Шаг 2: Вычисление кумулятивной суммы
    val cdf = histogram.cumulativeSum()

    // Шаг 3: Нормализация и применение эквализации
    val cdfMin = cdf.firstOrNull { it > 0 } ?: 0
    for (y in 0 until bitmap.height) {
        for (x in 0 until bitmap.width) {
            val color = bitmap.getPixel(x, y)
            val brightness = Color.red(color)
            val equalizedBrightness = ((cdf[brightness] - cdfMin) * 255 / (bitmap.width * bitmap.height)).coerceIn(0, 255)
            result.setPixel(x, y, Color.rgb(equalizedBrightness, equalizedBrightness, equalizedBrightness))
        }
    }
    return result
}

// Функция для вычисления кумулятивной суммы массива
fun IntArray.cumulativeSum(): IntArray {
    val cdf = IntArray(this.size)
    cdf[0] = this[0]
    for (i in 1 until this.size) {
        cdf[i] = cdf[i - 1] + this[i]
    }
    return cdf
}