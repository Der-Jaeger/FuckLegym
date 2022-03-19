package ldh.logic.clouds.service

import ldh.logic.clouds.model.StopConfig
import retrofit2.http.GET


/**
 * @author ldh
 * 时间: 2022/3/19 8:52
 * 邮箱: 2637614077@qq.com
 */
interface CloudConfigService {

    @GET("config/stop.json")
    suspend fun getStopConfig(): StopConfig

}