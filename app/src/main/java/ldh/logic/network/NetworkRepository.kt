package ldh.logic.network

import ldh.logic.OnlineData
import ldh.logic.OnlineData.headerMap
import ldh.logic.network.model.HttpResult
import ldh.logic.network.model.login.LoginRequestBean
import ldh.logic.network.model.running.RunningLimitRequestBean
import ldh.logic.network.model.running.UploadRunningDetailsRequestBean
import ldh.logic.network.service.EducationService
import ldh.logic.network.service.LoginService
import ldh.logic.network.service.RunningService


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
     * 所有的网络请求都统一经过这个函数
     */
    private suspend fun <T> catchError(block: suspend () -> HttpResult<T?>): HttpResult<T?> {
        return try {
            var result = block()
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