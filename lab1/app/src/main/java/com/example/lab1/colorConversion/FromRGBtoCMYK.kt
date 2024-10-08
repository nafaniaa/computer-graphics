package com.example.lab1.colorConversion

import com.example.lab1.models.Cmyk

fun rgbToCmyk(r: Int, g: Int, b: Int): Cmyk {
    val rNorm = r / 255.0
    val gNorm = g / 255.0
    val bNorm = b / 255.0

    val k = 1 - maxOf(rNorm, gNorm, bNorm)
    val c = (1 - rNorm - k) / (1 - k)
    val m = (1 - gNorm - k) / (1 - k)
    val y = (1 - bNorm - k) / (1 - k)

    return Cmyk(c, m, y, k)
}