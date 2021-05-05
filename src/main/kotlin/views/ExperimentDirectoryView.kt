package views

import core.ExperimentDirectory
import javafx.scene.control.Label
import javafx.scene.paint.Color
import java.io.File

class ExperimentDirectoryView(experimentDirectory: ExperimentDirectory) {

    val name = Label()
    val date = Label()

    init {
        val sourceFile = File(experimentDirectory.sourcePath)
        name.text = sourceFile.name + "\t"
        name.textFill =  if(experimentDirectory.isValid) Color.BLACK else Color.RED
        date.text = experimentDirectory.resultSummary.startTime.toString()
        date.textFill = Color.GRAY

    }
}