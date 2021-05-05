package views

import javafx.scene.control.Label

//There was no point in creating a core class for it so it can be found in the core package too.
class EpochView(id: Int, val logInfo: String) {
    val value = Label("$id  $logInfo")
}