package com.example.lab1.colorConversion

import com.example.lab1.models.Hsv

fun rgbToHsv(r: Int, g: Int, b: Int): Hsv {
    val rNorm = r / 255.0
    val gNorm = g / 255.0
    val bNorm = b / 255.0

    val max = maxOf(rNorm, gNorm, bNorm)
    val min = minOf(rNorm, gNorm, bNorm)
    val delta = max - min

    val h = when {
        delta == 0.0 -> 0.0
        max == rNorm -> 60 * (((gNorm - bNorm) / delta) % 6)
        max == gNorm -> 60 * (((bNorm - rNorm) / delta) + 2)
        else -> 60 * (((rNorm - gNorm) / delta) + 4)
    }
    val s = if (max == 0.0) 0.0 else delta / max
    val v = max

    return Hsv(h, s, v)
}