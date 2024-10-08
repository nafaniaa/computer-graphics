package com.example.lab1.colorConversion

import com.example.lab1.models.Rgb

fun hsvToRgb(h: Double, s: Double, v: Double): Rgb {
    val c = v * s
    val x = c * (1 - Math.abs((h / 60) % 2 - 1))
    val m = v - c

    val (rPrime, gPrime, bPrime) = when {
        h in 0.0..60.0 -> Triple(c, x, 0.0)
        h in 60.0..120.0 -> Triple(x, c, 0.0)
        h in 120.0..180.0 -> Triple(0.0, c, x)
        h in 180.0..240.0 -> Triple(0.0, x, c)
        h in 240.0..300.0 -> Triple(x, 0.0, c)
        else -> Triple(c, 0.0, x)
    }

    val r = (rPrime + m) * 255
    val g = (gPrime + m) * 255
    val b = (bPrime + m) * 255

    return Rgb(r.toInt(), g.toInt(), b.toInt())
}