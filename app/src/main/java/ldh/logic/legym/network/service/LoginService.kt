package ldh.logic.legym.network.service

import ldh.logic.legym.network.model.login.LoginRequestBean
import ldh.logic.legym.network.model.login.LoginResult
import ldh.logic.legym.network.model.HttpResult
import retrofit2.http.Body
import retrofit2.http.POST


/**
 * @author ldh
 * 时间: 2022/3/18 12:54
 * 邮箱: 2637614077@qq.com
 */
interface LoginService {

    /**
     * 登录
     */
    @POST("/authorization/user/manage/login")
    suspend fun login(@Body requestBean: LoginRequestBean): HttpResult<LoginResult?>

}