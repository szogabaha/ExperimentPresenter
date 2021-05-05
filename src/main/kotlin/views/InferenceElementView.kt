package views

import com.example.format
import javafx.scene.control.Label
import javafx.scene.paint.Color


class InferenceElementView(inferenceElement: Pair<String, Double>) {
    val language = Label(inferenceElement.component1())
    val accuracy = Label((inferenceElement.component2() * 100).format(2))

    init {
        accuracy.textFill = findColorFor(inferenceElement.component2())
        language.prefWidth = 30.0
    }

    companion object {
        fun findColorFor(value: Double): Color {
            return when (value) {
                in 0.0..0.3 -> Color.RED
                in 0.3..0.5 -> Color.SALMON
                in 0.5..0.7 -> Color.BLACK
                in 0.7..0.9 -> Color.LIGHTGREEN
                in 0.9..1.0 -> Color.GREEN
                else -> Color.BLACK
            }
        }
    }
}