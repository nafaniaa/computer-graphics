import java.awt.*
import java.awt.event.*
import java.awt.image.BufferedImage
import javax.swing.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class ImageEdit {
    var mode = 0
    var xPad = 0
    var xf = 0
    var yf = 0
    var yPad = 0
    var gridWidth = 30
    var pressed = false
    var startTime: Long = 0
    var endTime: Long = 0
    var SbSt: Long = 0
    var Brt: Long = 0
    var mainColor: Color = Color.BLACK
    lateinit var frame: JFrame
    lateinit var panel: MyPanel
    lateinit var image: BufferedImage
    var width = 0
    var height = 0

    val beginLSbS = mutableListOf<Pair<Double, Double>>()
    val endLSbS = mutableListOf<Pair<Double, Double>>()
    val beginLBr = mutableListOf<Pair<Double, Double>>()
    val endLBr = mutableListOf<Pair<Double, Double>>()

    init {
        frame = JFrame("Графический редактор").apply {
            setSize(350, 350)
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            jMenuBar = createMenuBar()
            layout = null
            addComponentListener(object : ComponentAdapter() {
                override fun componentResized(evt: ComponentEvent?) {
                    resizePanel()
                }
            })
        }

        panel = MyPanel().apply {
            setBounds(30, 30, 260, 260)
            background = Color.WHITE
            isOpaque = true
            addMouseMotionListener(object : MouseMotionAdapter() {
                override fun mouseDragged(e: MouseEvent) {
                    if (pressed) {
                        val g2 = image.graphics as Graphics2D
                        g2.color = mainColor
                        when (mode) {
                            0 -> drawLineBr(g2, xPad, yPad, e.x, e.y, false)  // Pencil
                            1 -> { // Eraser
                                clearArrays()
                                g2.stroke = BasicStroke(1000000.0f)
                                drawLineBr(g2, xPad, yPad, e.x, e.y, false)
                                drawGrid(g2)
                            }
                        }
                        xPad = e.x
                        yPad = e.y
                    }
                    repaint()
                }
            })
            addMouseListener(object : MouseAdapter() {
                override fun mousePressed(e: MouseEvent) {
                    xPad = e.x
                    yPad = e.y
                    xf = e.x
                    yf = e.y
                    pressed = true
                }

                override fun mouseReleased(e: MouseEvent) {
                    val g2 = image.graphics as Graphics2D
                    g2.color = mainColor
                    when (mode) {
                        1 -> {
                            clearArrays()
                            g2.stroke = BasicStroke(1000000.0f)
                            drawLineBr(g2, xPad, yPad, xPad, yPad, false)
                            drawGrid(g2)
                        }
                        2 -> drawLineSbS(g2, xf, yf, e.x, e.y, false)
                        4 -> drawLineBr(g2, xf, yf, e.x, e.y, false)
                    }
                    pressed = false
                    repaint()
                }
            })
        }

        frame.apply {
            add(panel)
            isVisible = true
        }
    }

    private fun createMenuBar(): JMenuBar {
        return JMenuBar().apply {
            val gridMenu = JMenu("Сетка")
            val widthMenu = JMenuItem(AbstractAction("Ширина") {
                val newGridWidth = JOptionPane.showInputDialog("Введите значение:")?.toIntOrNull()
                newGridWidth?.let {
                    gridWidth = it
                    val g2 = image.graphics as Graphics2D
                    g2.color = Color.WHITE
                    g2.fillRect(0, 0, panel.width, panel.height)
                    drawGrid(g2)
                    panel.repaint()
                }
            })
            gridMenu.add(widthMenu)

            val lineMenu = JMenu("Линия")
            lineMenu.add(JMenuItem(AbstractAction("Пошаговая") {
                val input = JOptionPane.showInputDialog("Введите x1 y1 x2 y2:")
                val coords = input?.split(" ")?.map { it.toIntOrNull() }
                if (coords != null && coords.size == 4) {
                    val (x1, y1, x2, y2) = coords.map { it ?: 0 }
                    beginLSbS.add(Pair(x1.toDouble() / width * gridWidth, y1.toDouble() / height * gridWidth))
                    endLSbS.add(Pair(x2.toDouble() / width * gridWidth, y2.toDouble() / height * gridWidth))
                    drawLineSbS(image.graphics as Graphics2D, x1, y1, x2, y2, true)
                    startTime = System.nanoTime()
                    drawLineSbS(image.graphics as Graphics2D, x1 * gridWidth, y1 * gridWidth, x2 * gridWidth, y2 * gridWidth, false)
                    endTime = System.nanoTime()
                    SbSt = endTime - startTime
                    panel.repaint()
                }
            }))

            lineMenu.add(JMenuItem(AbstractAction("Брезенхем") {
                val input = JOptionPane.showInputDialog("Введите x1 y1 x2 y2:")
                val coords = input?.split(" ")?.map { it.toIntOrNull() }
                if (coords != null && coords.size == 4) {
                    val (x1, y1, x2, y2) = coords.map { it ?: 0 }
                    beginLBr.add(Pair(x1.toDouble() / width * gridWidth, y1.toDouble() / height * gridWidth))
                    endLBr.add(Pair(x2.toDouble() / width * gridWidth, y2.toDouble() / height * gridWidth))
                    drawLineBr(image.graphics as Graphics2D, x1, y1, x2, y2, true)
                    startTime = System.nanoTime()
                    drawLineBr(image.graphics as Graphics2D, x1 * gridWidth, y1 * gridWidth, x2 * gridWidth, y2 * gridWidth, false)
                    endTime = System.nanoTime()
                    Brt = endTime - startTime
                    panel.repaint()
                }
            }))

            lineMenu.add(JMenuItem(AbstractAction("Время") {
                JOptionPane.showMessageDialog(null, "Пошаговый метод: $SbSt ns\nМетод Брезенхема: $Brt ns")
            }))

            add(gridMenu)
            add(lineMenu)
        }
    }

    private fun resizePanel() {
        panel.setSize(frame.width - 40, frame.height - 80)
        val tempImage = BufferedImage(panel.width, panel.height, BufferedImage.TYPE_INT_RGB)
        val g2 = tempImage.createGraphics()
        g2.color = Color.WHITE
        g2.fillRect(0, 0, panel.width, panel.height)
        width = panel.width
        height = panel.height
        drawGrid(g2)
        image = tempImage
        panel.repaint()
    }

    private fun drawLineSbS(g2: Graphics2D, x1: Int, y1: Int, x2: Int, y2: Int, bigD: Boolean) { /* ... */ }
    private fun drawLineBr(g2: Graphics2D, x1: Int, y1: Int, x2: Int, y2: Int, bigD: Boolean) { /* ... */ }
    private fun drawGrid(g2: Graphics2D) { /* ... */ }
    private fun clearArrays() { /* ... */ }

    inner class MyPanel : JPanel() {
        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            g.drawImage(image, 0, 0, null)
        }
    }
}

fun main() {
    EventQueue.invokeLater { ImageEdit() }
}
