package controllers

import views.InferenceElementView
import core.Inference
import javafx.scene.Node
import javafx.scene.chart.XYChart
import javafx.scene.control.ListView
import javafx.scene.layout.HBox

fun ListView<HBox>.refreshInferences(inference: Inference) {
    this.items.clear()
    inference.languageResults.forEach{
        val elementView = InferenceElementView(Pair(it.key, it.value))
        val hBox = HBox(elementView.language, elementView.accuracy)
        hBox.spacing = 50.0
        this.items.add(hBox)
    }
}

fun<T> Node.initializeChart(): T where T : XYChart<*,*>{
    @Suppress("UNCHECKED_CAST")
    val casted = this as T
    casted.animated = false
    this.isLegendVisible = false
    return this
}
