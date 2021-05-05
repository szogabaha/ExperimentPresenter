package services

import com.example.format
import views.EpochView
import java.io.File
import java.lang.Exception

object LogDao {
    private const val subStringForEpochNumber = "[train][INFO] - Epoch: "
    private const val subStringForDevAccuracy = "dev accuracy: "
    private const val subStringForDevLoss = " dev loss:"
    private const val logDelimiter = "-"

    fun getEpochs(logFile: File): List<EpochView> {
       try {
           val lines = mutableListOf<String>()
           logFile.forEachLine {
               lines.add(it)
           }
           val epochIds = lines.filter { it.contains(subStringForEpochNumber) }.map { getEpochIdFrom(it) }
           val devInformation = lines.filter { it.contains(subStringForDevAccuracy) and it.contains(subStringForDevLoss) }
               .map { cropEpochDevInfo(it) }

           val epochs = mutableListOf<EpochView>()
           epochIds.zip(devInformation).forEach { pair ->
               epochs.add(EpochView(pair.component1(), pair.component2()))
           }
           return epochs
       } catch (ex : Exception) {
           throw NoSuchElementException()
       }
    }

    private fun getEpochIdFrom(idLogLine : String) : Int {
        return idLogLine.substringAfter(subStringForEpochNumber, "0").toInt().or(0)
    }

    private fun cropEpochDevInfo(logLine : String) : String {
        val cropped =  logLine.substringAfter(logDelimiter)
        val accuracy = cropped.substringAfter(subStringForDevAccuracy).substringBefore(subStringForDevLoss).toDouble()
        val loss = cropped.substringAfter(subStringForDevLoss).toDouble()
        return "accuracy: ${accuracy.format(6)} loss: ${loss.format(6)}"
    }
}