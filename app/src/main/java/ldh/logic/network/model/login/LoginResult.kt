package ldh.logic.network.model.login

/**
 * 登录成功后返回的用户信息
 */
data class LoginResult(
    val accessToken: String,
    val accessTokenExpires: Long,
    val accountNumber: String,
    val authorities: List<Any>,
    val avatar: Any,
    val birthDay: Long,
    val campusId: String,
    val campusName: Any,
    val faceId: Any,
    val gender: Int,
    val height: Int,
    val id: String,
    val identity: String,
    val mobile: String,
    val nickName: String,
    val organizationId: String,
    val organizationName: String,
    val organizationUserNumber: String,
    val organizationVerified: Int,
    val realName: String,
    val refreshToken: String,
    val schoolId: String,
    val schoolName: String,
    val semesterId: Any,
    val tokenType: String,
    val userId: Any,
    val uuasId: Any,
    val weight: Int,
    val year: Int
)