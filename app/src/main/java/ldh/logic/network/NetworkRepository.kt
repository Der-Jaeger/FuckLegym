package ldh.logic.network

import fucklegym.top.entropy.User
import ldh.logic.network.model.login.LoginRequestBean
import ldh.logic.network.model.login.base.HttpResult
import ldh.logic.network.service.LoginService
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

    val loginService by lazy { ServiceCreator.create<LoginService>() }

    /**
     * 进行登录
     */
    suspend fun login(userId: String?, password: String?) = catchError {
        loginService.login(LoginRequestBean(password = password, userName = userId))
    }
    
    /**
     * 上传跑步数据。
     */
    @Throws
    @Deprecated("这里是基于以前的User来设计的，以后应该重写")
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

    private suspend fun <T> catchError(block: suspend () -> HttpResult<T?>): HttpResult<T?> {
        return try {
            block()
        } catch (e: Exception) {
            if (e is retrofit2.HttpException) {
                HttpResult(
                    code = e.code(),
                    exception = e,
                    message = e.message().toString(),
                    data = null
                )
            } else {
                HttpResult(code = 1000, exception = e, message = e.message ?: "", data = null)
            }
        }
    }


}