package controllers

import views.ResultSummaryView
import views.ExperimentDirectoryView
import core.ExperimentDirectory
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.chart.ScatterChart
import javafx.scene.chart.XYChart
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.layout.HBox
import javafx.stage.Stage

//This class is intended to help in separating the functionalities between the two views
class ComparisonController {

    private companion object {
        lateinit var mainStage: Stage
        lateinit var previousScene: Scene
        lateinit var firstDirectory: ExperimentDirectory
        lateinit var secondDirectory: ExperimentDirectory
    }
    @Suppress("UNCHECKED_CAST")
    fun start(mainStage: Stage,firstDirectory: ExperimentDirectory,secondDirectory: ExperimentDirectory ){
        ComparisonController.mainStage = mainStage
        previousScene = mainStage.scene
        ComparisonController.firstDirectory = firstDirectory
        ComparisonController.secondDirectory= secondDirectory

        val newParent: Parent =
            FXMLLoader.load(ComparisonController::class.java.classLoader.getResource("views/ComparisonView.fxml"))
        val scene = Scene(newParent)
        mainStage.scene = scene
        mainStage.show()

        val inference1 = scene.lookup("#fxinfcomp1") as ListView<HBox>
        inference1.refreshInferences(firstDirectory.inference)

        val inference2 = scene.lookup("#fxinfcomp2") as ListView<HBox>
        inference2.refreshInferences(secondDirectory.inference)

        setScatterPlot()
        setResultSummary(firstDirectory, "#fxnameone","#fxaccone", "#fxtimeone")
        setResultSummary(secondDirectory, "#fxnametwo","#fxacctwo", "#fxtimetwo")

    }

    private fun setResultSummary(dir: ExperimentDirectory,nameSelector: String, accuracySelector: String, durationSelector: String) {
        val view1  = ResultSummaryView(devAccLabel = mainStage.scene.lookup(accuracySelector) as Label, runningTimeLabel = mainStage.scene.lookup(durationSelector) as Label)
        view1.setResultSummary(dir)

        val nameLabel = mainStage.scene.lookup(nameSelector) as Label
        nameLabel.text = ExperimentDirectoryView(dir).name.text

    }

    private fun setScatterPlot() {
        val chart = mainStage.scene.lookup("#fxscatterchart").initializeChart<ScatterChart<Double, Double>>()
        val series1 = XYChart.Series<Double, Double>()

        if(firstDirectory.inference.isValid and secondDirectory.inference.isValid){
            val resultsFrom1 = firstDirectory.inference.languageResults
            val resultsFrom2 = secondDirectory.inference.languageResults

            var languages = resultsFrom1.keys.toList()
            languages = languages.filter { resultsFrom2.keys.contains(it)}

            languages.forEach {
                series1.data.add(XYChart.Data(resultsFrom1[it], resultsFrom2[it]))
            }

            chart.data.addAll(series1)
        }
    }



    fun pressStepBackButton() {
        mainStage.scene = previousScene
        mainStage.show()
    }


}