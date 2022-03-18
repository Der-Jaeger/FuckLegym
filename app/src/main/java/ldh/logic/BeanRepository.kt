package ldh.logic

import android.util.Log
import central.stu.fucklegym.Encrypter
import fucklegym.top.entropy.NetworkSupport
import fucklegym.top.entropy.PathGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ldh.config.AppConfig.LEGYM_APP_VERSION
import ldh.config.AppConfig.legymDateStringFormatter
import ldh.logic.network.model.running.RoutineLine
import ldh.logic.network.model.running.UploadRunningDetailsRequestBean
import ldh.ui.run.logic.RunningType
import ldh.utils.SystemUtil.systemModel
import ldh.utils.SystemUtil.systemVersion
import java.util.*
import kotlin.random.Random


/**
 * @author ldh
 * 时间: 2022/3/18 15:57
 * 邮箱: 2637614077@qq.com
 */
object BeanRepository {

    /**
     * 每公里消耗卡路里
     */
    private const val CALORIE_PER_MILEAGE = 58.3f

    /**
     * 生成跑步数据
     */
    fun generateRunningDetails(
        distanceRange: List<Float>,
        map: String?,
        limitationsGoalsSexInfoId: String?,
        type: RunningType
    ): UploadRunningDetailsRequestBean {
        //总共跑了多少
        val totMileage = distanceRange.let {
            Random.nextDouble(
                it.first().toDouble(),
                it.last().toDouble()
            )
        }
        val endTime = Date()
        val startTime =
            Date(endTime.time - (10 + Random.nextInt(10)) * 60 * 1000 - Random.nextInt(60) * 1000)
        val avePace =
            (((endTime.time - startTime.time) / 1000f / totMileage).toInt() * 1000).toDouble()
        val keepTime = (endTime.time - startTime.time).toInt() / 1000
        val calorie = (totMileage * CALORIE_PER_MILEAGE).toInt()
        val pace: Double = 0.5 + Random.nextInt(6) / 10.0
        val paceNumber = (totMileage * 1000 / pace / 2).toInt()
        val runPoints = ArrayList<RoutineLine>()
        try {
            val genPoints = PathGenerator.genRegularRoutine(map, totMileage)
            genPoints.forEach {
                runPoints.add(RoutineLine(it.key, it.value))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return UploadRunningDetailsRequestBean(
            appVersion = LEGYM_APP_VERSION,
            avePace = avePace,
            calorie = calorie,
            deviceType = systemModel,
            effectiveMileage = totMileage,
            effectivePart = 1,
            endTime = legymDateStringFormatter.format(endTime),
            gpsMileage = totMileage,
            keepTime = keepTime,
            limitationsGoalsSexInfoId = limitationsGoalsSexInfoId,
            paceNumber = paceNumber,
            paceRange = pace,
            routineLine = runPoints,
            scoringType = 1,
            semesterId = OnlineData.currentData.id,
            signDigital = Encrypter.getSha1(
                totMileage
                    .toString() + "1"
                        + legymDateStringFormatter.format(startTime)
                        + calorie
                        + avePace
                        + keepTime
                        + paceNumber
                        + totMileage
                        + "1" + Encrypter.run_salt
            ),
            signPoint = listOf(),
            startTime = legymDateStringFormatter.format(startTime),
            systemVersion = systemVersion,
            totalMileage = totMileage,
            totalPart = 0.0,
            type = type.title,
            uneffectiveReason = ""
            )
    }

}