package views

import com.example.format
import core.ExperimentDirectory
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.scene.control.Label

//These can be null because not all of these nodes are used by all the views
class ResultSummaryView(
    devAccLabel: Label? = null,
    devLossLabel: Label? = null,
    runningTimeLabel: Label? = null,
    startTimeLabel: Label? = null,
    location: Label? = null
) {
    private val devAcc: StringProperty = devAccLabel?.textProperty() ?: SimpleStringProperty()
    private val devLoss: StringProperty = devLossLabel?.textProperty() ?: SimpleStringProperty()
    private val runningTime: StringProperty = runningTimeLabel?.textProperty() ?: SimpleStringProperty()
    private val startTime: StringProperty = startTimeLabel?.textProperty() ?: SimpleStringProperty()
    private val location: StringProperty = location?.textProperty() ?: SimpleStringProperty("Not found")

    fun setResultSummary(directory: ExperimentDirectory) {
        startTime.value = START_CONST + directory.resultSummary.startTime.toString()
        devAcc.value = ACC_CONST + directory.resultSummary.devAcc.format(4)
        devLoss.value = LOSS_CONST + directory.resultSummary.devLoss.format(4)
        runningTime.value = DURATION_CONST + "${directory.resultSummary.runningTime.format(2)} seconds"
        location.value = ROOT_LOCATION_CONST + directory.sourcePath
    }

    companion object {
        const val START_CONST = "Start date: "
        const val ACC_CONST = "Development accuracy\n\t"
        const val LOSS_CONST: String = "Development loss\n\t"
        const val DURATION_CONST = "Training time: "
        const val ROOT_LOCATION_CONST = "Root location: \n"
    }
}
