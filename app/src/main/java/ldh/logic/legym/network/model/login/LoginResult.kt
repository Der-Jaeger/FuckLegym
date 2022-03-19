package ldh.logic.legym.network.model.login

/**
 * 登录成功后返回的用户信息
 */
data class LoginResult(
    val accessToken: String,
    val accessTokenExpires: Long,
    val accountNumber: String,
    val authorities: List<Any>,
    val avatar: Int?,
    val birthDay: Long,
    val campusId: String,
    val campusName: Any,
    val faceId: String?,
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
    val semesterId: String?,
    val tokenType: String,
    val userId: String?,
    val uuasId: String?,
    val weight: Int,
    val year: Int
)