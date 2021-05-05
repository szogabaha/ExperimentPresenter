package core

import java.util.Date
import kotlin.collections.HashMap

class ResultSummary(
    val devAcc: Double,
    val devLoss: Double,
    val runningTime: Double,
    val startTime: Date,
) {

    companion object {
        fun getResultSummaryFrom(map: HashMap<String, Any>): ResultSummary {
            val devAcc: Double = (map["dev_acc"] ?: 0.0) as Double
            val devLoss: Double = (map["dev_loss"]  ?: 0.0) as Double
            val runningTime: Double = (map["running_time"]  ?: 0.0) as Double
            val startTime: Date = (map["start_time"] ?: Date()) as Date

            return ResultSummary(devAcc, devLoss, runningTime, startTime)
        }

        fun createNullObject(): ResultSummary{
            return ResultSummary(0.0,0.0,-1.0, Date())
        }
    }


}
