package ldh.ui.login.logic

import com.tencent.mmkv.MMKV


/**
 * @author ldh
 * 时间: 2022/3/17 21:57
 * 邮箱: 2637614077@qq.com
 */
object LocalUserData {

    private const val KEY_USER_ID = "KEY_USER_ID"
    private const val KEY_USER_PASSWORD = "KEY_USER_PASSWORD"

    /**
     * 储存在本地的账号
     */
    var userId: String?
        get() = MMKV.defaultMMKV().decodeString(KEY_USER_ID)
        set(value) {
            MMKV.defaultMMKV().encode(KEY_USER_ID, value)
        }


    /**
     * 储存在本地的密码
     */
    var password: String?
        get() = MMKV.defaultMMKV().decodeString(KEY_USER_PASSWORD)
        set(value) {
            MMKV.defaultMMKV().encode(KEY_USER_PASSWORD, value)
        }
}