import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LineAlgorithmsApp()
        }
    }
}

@Composable
fun LineAlgorithmsApp() {
    var startX by remember { mutableStateOf(100f) }
    var startY by remember { mutableStateOf(100f) }
    var endX by remember { mutableStateOf(400f) }
    var endY by remember { mutableStateOf(400f) }
    var algorithmMode by remember { mutableStateOf(0) } // 0 - Step, 1 - DDA, 2 - Bresenham Line, 3 - Bresenham Circle

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { algorithmMode = 0 }) { Text("Пошаговый") }
            Button(onClick = { algorithmMode = 1 }) { Text("ЦДА") }
            Button(onClick = { algorithmMode = 2 }) { Text("Брезенхем (отрезок)") }
            Button(onClick = { algorithmMode = 3 }) { Text("Брезенхем (окружность)") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Canvas(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)) {
            when (algorithmMode) {
                0 -> drawStepByStep(startX, startY, endX, endY)
                1 -> drawDDA(startX, startY, endX, endY)
                2 -> drawBresenhamLine(startX, startY, endX, endY)
                3 -> drawBresenhamCircle(startX, startY, (endX - startX).toInt())
            }
        }
    }
}

// Пошаговый алгоритм
fun DrawScope.drawStepByStep(x1: Float, y1: Float, x2: Float, y2: Float) {
    val dx = x2 - x1
    val dy = y2 - y1
    val k = dy / dx
    val b = y1 - k * x1
    if (k.isFinite()) {
        for (x in x1.toInt()..x2.toInt()) {
            val y = k * x + b
            drawPoint(x.toFloat(), y.toFloat())
        }
    } else {
        for (y in y1.toInt()..y2.toInt()) {
            drawPoint(x1, y.toFloat())
        }
    }
}

// Алгоритм ЦДА (DDA)
fun DrawScope.drawDDA(x1: Float, y1: Float, x2: Float, y2: Float) {
    val dx = x2 - x1
    val dy = y2 - y1
    val steps = maxOf(dx.absoluteValue, dy.absoluteValue).toInt()
    val xIncrement = dx / steps
    val yIncrement = dy / steps
    var x = x1
    var y = y1
    for (i in 0..steps) {
        drawPoint(x, y)
        x += xIncrement
        y += yIncrement
    }
}

// Алгоритм Брезенхема для отрезка
fun DrawScope.drawBresenhamLine(x1: Float, y1: Float, x2: Float, y2: Float) {
    var x1 = x1.toInt()
    var y1 = y1.toInt()
    val x2 = x2.toInt()
    val y2 = y2.toInt()
    val dx = (x2 - x1).absoluteValue
    val dy = (y2 - y1).absoluteValue
    val sx = if (x1 < x2) 1 else -1
    val sy = if (y1 < y2) 1 else -1
    var err = dx - dy
    while (true) {
        drawPoint(x1.toFloat(), y1.toFloat())
        if (x1 == x2 && y1 == y2) break
        val e2 = 2 * err
        if (e2 > -dy) {
            err -= dy
            x1 += sx
        }
        if (e2 < dx) {
            err += dx
            y1 += sy
        }
    }
}

// Алгоритм Брезенхема для окружности
fun DrawScope.drawBresenhamCircle(xc: Float, yc: Float, radius: Int) {
    var x = 0
    var y = radius
    var d = 3 - 2 * radius
    drawCirclePoints(xc, yc, x, y)
    while (y >= x) {
        x++
        if (d > 0) {
            y--
            d += 4 * (x - y) + 10
        } else {
            d += 4 * x + 6
        }
        drawCirclePoints(xc, yc, x, y)
    }
}

// Вспомогательная функция для отрисовки точек окружности
fun DrawScope.drawCirclePoints(xc: Float, yc: Float, x: Int, y: Int) {
    listOf(
        xc + x to yc + y, xc - x to yc + y, xc + x to yc - y, xc - x to yc - y,
        xc + y to yc + x, xc - y to yc + x, xc + y to yc - x, xc - y to yc - x
    ).forEach { (px, py) -> drawPoint(px, py) }
}

// Вспомогательная функция для отрисовки одной точки
fun DrawScope.drawPoint(x: Float, y: Float, color: Color = Color.Black) {
    drawCircle(color = color, radius = 2f, center = androidx.compose.ui.geometry.Offset(x, y))
}