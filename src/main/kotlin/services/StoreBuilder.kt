package services

import core.ExperimentDirectory
import java.io.File
import java.io.FileOutputStream

//TODO We could store stuff in a database later.. For now I'm just saving the locations of the added experiment directories
 object StoreBuilder {

    private const val folderNameOfSavedPaths = "savedDirectories"

    fun saveToFile(experimentDirectory: ExperimentDirectory) {
        FileOutputStream((folderNameOfSavedPaths),true).bufferedWriter().use { out ->
            out.append(experimentDirectory.sourcePath)
            out.newLine()
        }
    }

    fun loadFromFile(): List<String> {
        val paths = mutableListOf<String>()
        val sourceFile = File(folderNameOfSavedPaths)
        if (sourceFile.exists()) {
            File(folderNameOfSavedPaths).forEachLine {
                paths.add(it)
            }
        } else {
            sourceFile.createNewFile()
        }
        return paths
    }

    fun getDirectoryById(id: Int): ExperimentDirectory {
        val paths = loadFromFile()
        return ExperimentDirectory(File(paths[id]))
    }
}