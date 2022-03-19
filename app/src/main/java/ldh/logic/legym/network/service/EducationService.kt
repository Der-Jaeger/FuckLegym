package ldh.logic.legym.network.service

import ldh.logic.LegymHeaderMap
import ldh.logic.legym.network.model.HttpResult
import ldh.logic.legym.network.model.current.GetCurrentResultBean
import retrofit2.http.GET
import retrofit2.http.HeaderMap


/**
 * @author ldh
 * 时间: 2022/3/18 17:13
 * 邮箱: 2637614077@qq.com
 */
interface EducationService {

    @GET("/education/semester/getCurrent")
    suspend fun getCurrent(@HeaderMap headerMap: LegymHeaderMap): HttpResult<GetCurrentResultBean?>

}