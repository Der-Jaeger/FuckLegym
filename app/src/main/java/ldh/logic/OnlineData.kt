package ldh.logic

import android.content.Intent
import com.liangguo.androidkit.app.startNewActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ldh.logic.interfaces.LoginResultCallback
import ldh.logic.network.NetworkRepository
import ldh.logic.network.model.current.GetCurrentResultBean
import ldh.logic.network.model.login.LoginResult
import ldh.logic.network.model.running.RunningLimitRequestBean
import ldh.logic.network.model.running.RunningLimitResultBean
import ldh.ui.login.LoginActivity
import ldh.ui.login.logic.LocalUserData

/**
 * 乐健请求头的Map类型
 */
typealias LegymHeaderMap = MutableMap<String, String>

/**
 * @author ldh
 * 时间: 2022/3/17 17:24
 * 邮箱: 2637614077@qq.com
 */
object OnlineData {

    /**
     * 当前的用户
     */
    lateinit var userData: LoginResult

    lateinit var runningLimitData: RunningLimitResultBean

    /**
     * 请求头
     */
    val headerMap: LegymHeaderMap
        get() = mutableMapOf(
            Pair("Content-type", "application/json"),
            Pair("Authorization", "Bearer " + userData.accessToken)
        )

    private val loginDataFlow = MutableSharedFlow<LoginResult>()

    private val runningLimitFlow = MutableSharedFlow<RunningLimitResultBean>()

    private var currentDataFlow = MutableSharedFlow<GetCurrentResultBean>()

    /**
     * 获取用户，在回调中会传回一个非空的[LoginResult]对象。
     */
    @Deprecated("已废弃，以后删了")
    fun getUser(callback: LoginResultCallback) {
        userData?.let {
            callback.onResult(it)
        } ?: loginAndDo {
            userData?.let {
                callback.onResult(it)
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
                            loginDataFlow.emit(it)
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

    suspend fun syncLogin() =
        NetworkRepository.login(LocalUserData.userId, LocalUserData.password).apply {
            data?.let { loginDataFlow.emit(it) }
        }

    init {
        CoroutineScope(Dispatchers.IO).apply {
            launch {
                loginDataFlow.collect { loginResult ->
                    //登录之后要请求getCurrent
                    userData = loginResult
                    NetworkRepository.getCurrentSemesterId().data?.let {
                        currentDataFlow.emit(it)
                    }
                }
            }

            launch {
                currentDataFlow.collect { currentResultBean ->
                    //请求到当前数据后要请求跑步限制的信息
                    NetworkRepository.getRunningLimit(RunningLimitRequestBean(currentResultBean.id)).data?.let {
                        runningLimitFlow.emit(it)
                    }
                }
            }

            launch {
                runningLimitFlow.collect {
                    runningLimitData = it
                }
            }
        }
    }

}