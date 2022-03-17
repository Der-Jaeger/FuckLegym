package ldh.logic

import central.stu.fucklegym.FreeRun
import fucklegym.top.entropy.User
import ldh.ui.run.logic.RunningPrefUtil
import ldh.ui.run.logic.RunningType
import java.io.IOException
import java.util.*


/**
 * @author ldh
 * 时间: 2022/3/17 17:32
 * 邮箱: 2637614077@qq.com
 */
object NetworkRepository {

    /**
     * 上传跑步数据。
     */
    @Throws
    fun uploadRunningData(user: User) {
        //跑步的距离，在范围中取随机值。
        val distance = RunningPrefUtil.prefDistanceRange.let {
                kotlin.random.Random.nextDouble(
                    it.first().toDouble(),
                    it.last().toDouble()
                )
            }

        //跑步的地图
        val map = RunningPrefUtil.prefRunningMap

        //跑步类型
        val type = RunningType.getRunningTypeByPrefValue(RunningPrefUtil.prefRunningType).title

        val random = Random(System.currentTimeMillis())
        val endTime = Date()
        val startTime =
            Date(endTime.time - (10 + random.nextInt(10)) * 60 * 1000 - random.nextInt(60) * 1000)
        try {
            user.uploadRunningDetail(startTime, endTime, distance, distance, map, type)
        } catch (e: IOException) {
            throw e
        }
    }

}