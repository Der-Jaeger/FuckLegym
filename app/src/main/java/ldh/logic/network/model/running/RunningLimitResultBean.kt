package ldh.logic.network.model.running

data class RunningLimitResultBean(
    val averageCadenceEnd: Int,
    val averageCadenceStart: Int,
    val averageSpeedEnd: String,
    val averageSpeedStart: String,
    val currentTimeStamp: String,
    val dailyMileage: Double,
    val dailyParagraph: Any,
    val effectiveMileageEnd: Double,
    val effectiveMileageStart: Double,
    val effectiveParagraphEnd: Any,
    val effectiveParagraphMileage: Any,
    val effectiveParagraphStart: Any,
    val enableStatus: Any,
    val fixedPointNumber: Any,
    val fixedPointOrder: Any,
    val fixedPointPattern: Int,
    val fixedPointPercentage: Any,
    val freePattern: Int,
    val hasRule: Boolean,
    val limitationsGoalsSexInfoId: String,
    val notice: Any,
    val patternId: String,
    val runningTimeLimit: RunningTimeLimit,
    val scopePattern: Int,
    val scopePercentage: Int,
    val scoringType: Int,
    val semesterMileage: Int,
    val semesterParagraph: Any,
    val totalDayMileage: String,
    val totalDayPart: Int,
    val totalWeekMileage: String,
    val totalWeekPart: Int,
    val weeklyMileage: Double,
    val weeklyParagraph: Any
)

data class RunningTimeLimit(
    val endTime: String,
    val openTimes: List<OpenTime>,
    val startTime: String,
    val weekOpenTimes: List<WeekOpenTime>
)

data class OpenTime(
    val dayEndTime: String,
    val dayStartTime: String
)

data class WeekOpenTime(
    val dayEndTime: String,
    val dayStartTime: String,
    val week: String
)