package ldh.logic.legym.network.model

import java.lang.Exception


/**
 * @author ldh
 * 时间: 2022/3/18 12:56
 * 邮箱: 2637614077@qq.com
 *
 * 乐健接口返回的通用结果
 */
data class HttpResult<T>(
    val code: Int,
    val `data`: T?,
    val message: String?,
    val exception: Exception?
)