package services

import java.io.File
import java.util.SortedMap
import kotlin.NoSuchElementException

object InferenceDao {

    fun getInferenceMap(inferenceFile: File): SortedMap<String, Double> {
        try {
            val map = mutableMapOf<String, Double>()

            inferenceFile.forEachLine {
                val values = it.split(",")
                if (values.size == 2) {
                    map += Pair(values[0], values[1].toDouble())
                }
            }
            return map.toSortedMap()
        } catch (ex: Exception) {
            throw NoSuchElementException()
        }
    }
}