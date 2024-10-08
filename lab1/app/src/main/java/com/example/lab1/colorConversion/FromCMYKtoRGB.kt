package com.example.lab1.colorConversion

import com.example.lab1.models.Rgb

fun cmykToRgb(c: Double, m: Double, y: Double, k: Double): Rgb {
    val r = (1 - c) * (1 - k) * 255
    val g = (1 - m) * (1 - k) * 255
    val b = (1 - y) * (1 - k) * 255

    return Rgb(r.toInt(), g.toInt(), b.toInt())
}