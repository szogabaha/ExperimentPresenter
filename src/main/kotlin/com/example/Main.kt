package com.example

import javafx.application.Application
import controllers.MainController

fun main(args: Array<String>) {
    Application.launch(MainController::class.java, *args)

}

fun Double.format(digits: Int) = "%.${digits}f".format(this)
