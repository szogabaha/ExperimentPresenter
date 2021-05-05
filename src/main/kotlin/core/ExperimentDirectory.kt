package core

import services.InferenceDao
import services.LogDao
import views.EpochView
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.lang.Exception

class ExperimentDirectory(source: File) {

    var isValid = true
    var resultSummary: ResultSummary
    val sourcePath: String = source.path
    lateinit var epochs: List<EpochView>
    lateinit var inference: Inference

    init {
        try {
            val resultSummaryFile: File = source.listFiles()?.first { it.name == "result.yaml" } ?: File("")

            @Suppress("UNCHECKED_CAST")
            resultSummary =
                ResultSummary.getResultSummaryFrom(Yaml().load(resultSummaryFile.readText()) as HashMap<String, Any>)
        } catch (ex: Exception){
            resultSummary = ResultSummary.createNullObject()
            isValid = false
        }
        initializeEpochList(source)
        initializeInference(source)
    }

    private fun initializeEpochList(source: File){
        try {
            val trainLog : File = source.walkTopDown().maxDepth(3).filter { it.name == "train.log" }.first()
            epochs = LogDao.getEpochs(trainLog)
        } catch (ex : NoSuchElementException) {
            epochs = listOf(EpochView(0, "Log file not found"))
            isValid = false
        }

    }

    private fun initializeInference(source: File){
        try {
            val inferenceFile : File = source.walkTopDown().maxDepth(3).filter { it.name == "result_csv" }.first()
            val map = InferenceDao.getInferenceMap(inferenceFile)
            inference = Inference(map)
        } catch (ex : NoSuchElementException) {
            inference = Inference(sortedMapOf(Pair("not found", 0.0)), false)
            isValid = false
        }
    }
}