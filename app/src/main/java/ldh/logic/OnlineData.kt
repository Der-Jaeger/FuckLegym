package ldh.logic

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.liangguo.androidkit.app.startNewActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ldh.logic.network.NetworkRepository
import ldh.logic.network.model.login.LoginResult
import ldh.ui.login.LoginActivity
import ldh.ui.login.logic.LocalUserData


/**
 * @author ldh
 * 时间: 2022/3/17 17:24
 * 邮箱: 2637614077@qq.com
 */
object OnlineData {

    internal val userData = MutableLiveData<LoginResult>()

    /**
     * 当前的用户
     */
    val user: LoginResult?
        get() = userData.value

    /**
     * 获取用户，在回调中会传回一个非空的[LoginResult]对象。
     */
    fun getUser(block: (user: LoginResult) -> Unit) {
        user?.let {
            block(it)
        } ?: loginAndDo {
            user?.let {
                block(it)
            }
        }
    }

    /**
     * 重新登录并且在登录结束后做什么事情...
     *
     * 可以在任何线程中调用此函数，并且其中的[runnable]参数只会在主线程中执行。
     *
     * 如果重新登录成功，则会执行[runnable]，如果不成功，则清空当前Activity栈并打开[LoginActivity]。
     */
    fun loginAndDo(runnable: Runnable? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val id = LocalUserData.userId
                val password = LocalUserData.password
                if (id == null || password == null) throw NullPointerException()
                NetworkRepository.login(id, password).apply {
                    exception?.let {
                        //登录失败
                        throw it
                    }
                    data?.let {
                        //登录成功
                        withContext(Dispatchers.Main) {
                            userData.postValue(it)
                            runnable?.run()
                        }
                    }
                }
            } catch (e: Exception) {
                //登录不成功则需要回到登录界面重新登录并且不会执行runnable。
                LoginActivity::class.startNewActivity {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
            }
        }
    }

}