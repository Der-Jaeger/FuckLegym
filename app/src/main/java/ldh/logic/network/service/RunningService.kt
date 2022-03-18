package ldh.logic.network.service

import ldh.logic.LegymHeaderMap
import ldh.logic.network.model.HttpResult
import ldh.logic.network.model.running.RunningLimitRequestBean
import ldh.logic.network.model.running.RunningLimitResultBean
import ldh.logic.network.model.running.UploadRunningDetailsRequestBean
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST


/**
 * @author ldh
 * 时间: 2022/3/18 15:32
 * 邮箱: 2637614077@qq.com
 */
interface RunningService {

    @POST("running/app/uploadRunningDetails")
    suspend fun uploadRunningDetails(@HeaderMap headerMap: LegymHeaderMap, @Body requestBean: UploadRunningDetailsRequestBean): HttpResult<String?>

    @POST("running/app/getRunningLimit")
    suspend fun getRunningLimit(@HeaderMap headerMap: LegymHeaderMap, @Body requestBean: RunningLimitRequestBean): HttpResult<RunningLimitResultBean?>

}