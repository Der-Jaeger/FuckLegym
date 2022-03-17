package ldh.logic

import androidx.lifecycle.MutableLiveData
import fucklegym.top.entropy.User


/**
 * @author ldh
 * 时间: 2022/3/17 17:24
 * 邮箱: 2637614077@qq.com
 */
object OnlineData {

    /**
     * 当前已经登录的用户
     */
    val user = MutableLiveData<User>()

}