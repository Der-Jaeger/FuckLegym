package ldh.logic.clouds

import ldh.logic.clouds.model.StopConfig
import ldh.logic.clouds.service.CloudConfigService


/**
 * @author ldh
 * 时间: 2022/3/19 8:48
 * 邮箱: 2637614077@qq.com
 */
object CloudsNetworkRepository {

    private val cloudConfigService by lazy { CloudsServiceCreator.create<CloudConfigService>() }

    suspend fun getStopConfig(): StopConfig? = catchError { cloudConfigService.getStopConfig() }


    private suspend fun <T> catchError(block: suspend () -> T?): T? =
        try {
            block()
        } catch (e: Exception) {
            null
        }


}