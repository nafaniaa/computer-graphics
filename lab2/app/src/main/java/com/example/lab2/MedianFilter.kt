package com.example.lab2

import android.graphics.Bitmap

fun medianFilter(bitmap: Bitmap): Bitmap {
    val result = bitmap.copy(bitmap.config, true)
    // Применение медианного фильтра (например, с ядром 3x3)
    // Пройти по каждому пикселю, собрать значения в окрестности, отсортировать их и выбрать медиану
    // Примерный код для медианного фильтра:
    for (y in 1 until bitmap.height - 1) {
        for (x in 1 until bitmap.width - 1) {
            val pixels = mutableListOf<Int>()
            for (dy in -1..1) {
                for (dx in -1..1) {
                    pixels.add(bitmap.getPixel(x + dx, y + dy))
                }
            }
            pixels.sort()
            result.setPixel(x, y, pixels[pixels.size / 2])
        }
    }
    return result
}
