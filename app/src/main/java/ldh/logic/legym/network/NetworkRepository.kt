package ldh.logic.legym.network

import android.util.Log
import fucklegym.top.entropy.NetworkSupport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ldh.logic.legym.OnlineData
import ldh.logic.legym.OnlineData.headerMap
import ldh.logic.legym.network.model.HttpResult
import ldh.logic.legym.network.model.login.LoginRequestBean
import ldh.logic.legym.network.model.running.RunningLimitRequestBean
import ldh.logic.legym.network.model.running.UploadRunningDetailsRequestBean
import ldh.logic.legym.network.model.running.totalRunning.TotalRunningRequestBean
import ldh.logic.legym.network.service.EducationService
import ldh.logic.legym.network.service.LoginService
import ldh.logic.legym.network.service.RunningService
import ldh.ui.run.logic.RunningType
import java.io.IOException
import java.util.*
import kotlin.random.Random


/**
 * @author ldh
 * 时间: 2022/3/17 17:32
 * 邮箱: 2637614077@qq.com
 */
object NetworkRepository {

    private val loginService by lazy { ServiceCreator.create<LoginService>() }
    private val runningService by lazy { ServiceCreator.create<RunningService>() }
    private val educationService by lazy { ServiceCreator.create<EducationService>() }

    /**
     * 进行登录
     */
    suspend fun login(userId: String?, password: String?) = catchError {
        loginService.login(LoginRequestBean(password = password, userName = userId))
    }

    /**
     * 上传跑步数据
     */
    suspend fun uploadRunningDetail(requestBean: UploadRunningDetailsRequestBean) = catchError {
        runningService.uploadRunningDetails(headerMap, requestBean)
    }

    /**
     * 用以前的旧代码上传跑步数据
     */
    suspend fun uploadRunningDetail(
        distanceRange: List<Float>,
        map: String?,
        limitationsGoalsSexInfoId: String?,
        type: RunningType
    ) = withContext(Dispatchers.IO) {
        try {
            val totMileage = distanceRange.let {
                Random.nextDouble(
                    it.first().toDouble(),
                    it.last().toDouble()
                )
            }
            val endTime = Date()
            val startTime =
                Date(endTime.time - (10 + Random.nextInt(10)) * 60 * 1000 - Random.nextInt(60) * 1000)
            return@withContext NetworkSupport.uploadRunningDetail(
                OnlineData.userData.accessToken,
                limitationsGoalsSexInfoId,
                OnlineData.currentData.id,
                totMileage,
                totMileage,
                startTime,
                endTime,
                map,
                type.title
            ) == NetworkSupport.UploadStatus.SUCCESS
        } catch (e: IOException) {
            Log.e("上传失败", e.toString())
            return@withContext false
        }
    }

    suspend fun getCurrentSemesterId() = catchError {
        educationService.getCurrent(headerMap)
    }

    /**
     * 获取跑步的规则限制
     */
    suspend fun getRunningLimit(requestBean: RunningLimitRequestBean) = catchError {
        runningService.getRunningLimit(headerMap, requestBean)
    }

    /**
     * 获取已经跑了多少公里了
     */
    suspend fun getTotalRunning(requestBean: TotalRunningRequestBean) = catchError {
        runningService.getTotalRunning(headerMap, requestBean)
    }


    /**
     * 所有的网络请求都统一经过这个函数
     */
    private suspend fun <T> catchError(block: suspend () -> HttpResult<T?>): HttpResult<T?> {
        return try {
            var result = block()
            Log.d("login111", "catchError: " + result)
            if (result.code == 401 || result.code == 1002) {
                //Token失效，重新登录
                OnlineData.syncLogin().data?.let {
                    //登录成功，再请求一次
                    result = catchError { block() }
                }
            }
            result
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