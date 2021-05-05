package controllers

import views.ResultSummaryView
import views.ExperimentDirectoryView
import services.StoreBuilder
import views.EpochView
import core.ExperimentDirectory
import core.Inference
import javafx.application.Application
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.chart.BarChart
import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.layout.HBox
import javafx.stage.Stage
import javafx.stage.DirectoryChooser

import java.io.File


class MainController : Application() {

    companion object {
        lateinit var resultSummaryView: ResultSummaryView
        lateinit  var listOfExperimentDirectories: ListView<HBox>
        lateinit var viewOfEpochs: ListView<Label>
        lateinit var viewOfInference: ListView<HBox>
        lateinit var inferenceChart: BarChart<String, Double>
        lateinit var selectedDirectory : ExperimentDirectory
        lateinit var stage : Stage
        var firstDirectorySelected = false
        var comparisonPressed = false
    }

    //We can do this as we are not changing the types of nodes thus we can be certain that casting is possible
    @Suppress("UNCHECKED_CAST")
    override fun start(mainStage: Stage) {
        stage = mainStage
        stage.isResizable = false
        val root: Parent = FXMLLoader.load(MainController::class.java.classLoader.getResource("views/MainView.fxml"))
        val scene = Scene(root)
        stage.scene = scene
        stage.show()

        resultSummaryView = ResultSummaryView(
            scene.lookup("#fxAccuracy") as Label,
            scene.lookup("#fxLoss") as Label,
            scene.lookup("#fxDuration") as Label,
            scene.lookup("#fxStartDate") as Label,
            scene.lookup("#fxLocation") as Label
        )
        listOfExperimentDirectories = scene.lookup("#fxListOfDirectories") as ListView<HBox>
        viewOfEpochs = scene.lookup("#fxEpochs") as ListView<Label>
        viewOfInference = scene.lookup("#fxInference") as ListView<HBox>
        inferenceChart = scene.lookup("#inferenceChart").initializeChart()
        initializeListOfDirectories()

        stage.setOnCloseRequest {
            stop()
        }
    }

    @FXML
    fun pressCompareButton() {
        if (comparisonPressed) {
            enableComparisonButton()
        } else if (firstDirectorySelected and !comparisonPressed) {
            disableComparisonButton()
        }
    }

    private fun disableComparisonButton() {
        listOfExperimentDirectories.selectionModel.clearSelection()
        comparisonPressed = true
        stage.scene.lookup("#compare").style = "-fx-background-color: #ddd;"
    }

    private fun enableComparisonButton(clearSection: Boolean = true) {
        if (clearSection) {
            listOfExperimentDirectories.selectionModel.clearSelection()
        }
        comparisonPressed = false
        stage.scene.lookup("#compare").style = null
    }

    @FXML
    @Suppress("UNCHECKED_CAST") //I am so sorry its passed midnight
    fun addExperiment() {
        enableComparisonButton(clearSection = false)
        val directoryChooser = DirectoryChooser()
        val selectedDirectory = directoryChooser.showDialog(Stage())?: return

        val experimentDirectory = ExperimentDirectory(selectedDirectory)
        resultSummaryView.setResultSummary(experimentDirectory)
        StoreBuilder.saveToFile(experimentDirectory)

        val experimentDirectoryView = ExperimentDirectoryView(experimentDirectory)
        listOfExperimentDirectories.addExperimentDirectoryViewsToListView(experimentDirectoryView)
    }

    private fun initializeListOfDirectories(){
        val paths = StoreBuilder.loadFromFile()
        paths.forEach {
            val experimentDirectory = ExperimentDirectory(File(it))
            listOfExperimentDirectories.addExperimentDirectoryViewsToListView(ExperimentDirectoryView(experimentDirectory))
        }
    }

    // I kept this as a historical function as this is my first ever extension
    private fun ListView<HBox>.addExperimentDirectoryViewsToListView(experimentDirectoryView: ExperimentDirectoryView) {
        val stack = HBox()
        stack.children.addAll(experimentDirectoryView.name, experimentDirectoryView.date)
        this.items.add(stack)
    }

    @FXML
    fun selectDirectoryFromList() {
        val idOfSelectedDirectory = if(listOfExperimentDirectories.selectionModel.selectedIndices.size == 1) listOfExperimentDirectories.selectionModel.selectedIndex else return
        val previousDirectory = if(firstDirectorySelected) selectedDirectory else null

        refreshViewAfterSelectingDirectoryFromList(idOfSelectedDirectory)

        if(firstDirectorySelected and comparisonPressed) {
            enableComparisonButton(clearSection = false)
            val comparison = ComparisonController()
            //We technically don't need the elvis operator, but seemed better than banging the variable
            comparison.start(stage, previousDirectory?: return, selectedDirectory)
        }

        //Element chosen for the first time
        if (!firstDirectorySelected){
            enableComparisonButton(clearSection = false)
            firstDirectorySelected = true
        }
    }

    private fun refreshViewAfterSelectingDirectoryFromList(id: Int) {
        selectedDirectory = StoreBuilder.getDirectoryById(id)
        resultSummaryView.setResultSummary(selectedDirectory)
        refreshEpocViews(selectedDirectory.epochs)
        viewOfInference.refreshInferences(selectedDirectory.inference)
        refreshBarChart(selectedDirectory.inference)
    }

    private fun refreshEpocViews(labels : List<EpochView>){
        viewOfEpochs.items.clear()
        labels.forEach { viewOfEpochs.items.add(it.value) }
    }


    private fun refreshBarChart(inference: Inference) {
        inferenceChart.data.clear()
        if (!inference.isValid) {
            return
        }

        val dataSeries: Series<String, Double> = Series()
        inference.languageResults.forEach{
            dataSeries.data.add(XYChart.Data(it.key, it.value))
        }
        inferenceChart.data.add(dataSeries)
    }
}