package ldh.logic.clouds.service

import ldh.logic.clouds.model.Notice
import ldh.logic.clouds.model.StopConfig
import ldh.logic.clouds.model.UploadLog
import retrofit2.http.GET


/**
 * @author ldh
 * 时间: 2022/3/19 8:52
 * 邮箱: 2637614077@qq.com
 */
interface CloudConfigService {

    @GET("config/stop.json")
    suspend fun getStopConfig(): StopConfig

    @GET("config/notice.json")
    suspend fun getNotices(): List<Notice>

    @GET("config/log.json")
    suspend fun isEnableUploadLog(): UploadLog

}