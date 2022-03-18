package ldh.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


/**
 * @author ldh
 * 时间: 2022/3/18 11:22
 * 邮箱: 2637614077@qq.com
 */
class LoginViewModel: ViewModel() {

    /**
     * 同意软件协议
     */
    val agree = MutableLiveData(false)

}