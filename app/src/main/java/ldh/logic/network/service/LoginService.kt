package ldh.logic.network.service

import ldh.logic.network.model.login.LoginRequestBean
import ldh.logic.network.model.login.LoginResult
import ldh.logic.network.model.login.base.HttpResult
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