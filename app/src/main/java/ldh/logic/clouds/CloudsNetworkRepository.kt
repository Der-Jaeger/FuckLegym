package ldh.logic.clouds

import android.util.Log
import ldh.logic.clouds.model.StopConfig
import ldh.logic.clouds.service.CloudConfigService


/**
 * @author ldh
 * 时间: 2022/3/19 8:48
 * 邮箱: 2637614077@qq.com
 */
object CloudsNetworkRepository {

    private val cloudConfigService by lazy { CloudsServiceCreator.create<CloudConfigService>() }

    suspend fun getStopConfig() = catchError { cloudConfigService.getStopConfig() }

    suspend fun getNotices() = catchError { cloudConfigService.getNotices() }

    suspend fun isEnableUploadLog() = catchError { cloudConfigService.isEnableUploadLog() }

    private suspend fun <T> catchError(block: suspend () -> T?): T? =
        try {
            block()
        } catch (e: Exception) {
            Log.e("请求异常", e.toString())
            null
        }


}