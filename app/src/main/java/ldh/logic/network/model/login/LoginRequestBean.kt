package ldh.logic.network.model.login

/**
 * 登录请求的
 */
data class LoginRequestBean(
    /**
     * 未知，一般都是1
     */
    val entrance: Int = 1,
    val password: String?,
    val userName: String?
)