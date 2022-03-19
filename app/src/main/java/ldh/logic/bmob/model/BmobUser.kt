package ldh.logic.bmob.model

import cn.bmob.v3.BmobObject


/**
 * @author ldh
 * 时间: 2022/3/19 16:08
 * 邮箱: 2637614077@qq.com
 *
 * 每一个乐健账号会对应一个Bmob账号，Bmob账号比乐健的多一些属性。
 *
 * 记录了学校，学院，年级等信息方便查看app流通情况。
 */
data class BmobUser(
    val userId: String,
    /**
     * 账号余额
     */
    val integral: Int,
    val isVip: Boolean,
    val schoolName: String,
    val organizationName: String,
    val year: Int
) : BmobObject(BMOB_LEGYM_USER_TABLE)

/**
 * Bmob用户的表名
 */
const val BMOB_LEGYM_USER_TABLE = "LegymUser"
