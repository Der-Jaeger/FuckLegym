package ldh.logic

import android.content.Intent
import android.util.Log
import com.liangguo.androidkit.app.startNewActivity
import fucklegym.top.entropy.User
import kotlinx.coroutines.*
import ldh.logic.bmob.logic.generateBmobUser
import ldh.logic.bmob.logic.getBmobDataByLegymId
import ldh.logic.bmob.logic.suspendSaveSync
import ldh.logic.bmob.model.BmobUser
import ldh.logic.legym.LocalUserData
import ldh.logic.legym.network.NetworkRepository
import ldh.logic.legym.network.model.current.GetCurrentResultBean
import ldh.logic.legym.network.model.login.LoginResult
import ldh.logic.legym.network.model.running.RunningLimitRequestBean
import ldh.logic.legym.network.model.running.RunningLimitResultBean
import ldh.ui.login.LoginActivity
import ldh.utils.LogUtil.uploadLog


/**
 * @author ldh
 * 时间: 2022/3/17 17:24
 * 邮箱: 2637614077@qq.com`
 */
object OnlineData {

    /**
     * 当前的用户
     */
    private var userData: LoginResult? = null

    lateinit var runningLimitData: RunningLimitResultBean

    lateinit var currentData: GetCurrentResultBean

    lateinit var bmobUser: BmobUser

    val user: User
        get() = User(LocalUserData.userId, LocalUserData.password)

    /**
     * 获取当前登录的用户，若当前用户为null，则重新打开登录界面，否则直接调用[success]。
     */
    fun getUserDataOrRelogin(success: (LoginResult) -> Unit) {
        userData?.let {
            success(it)
        } ?: LoginActivity::class.startNewActivity {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
    }

    /**
     * 同步的方式获取当前用户
     *
     * 如果当前用户不为空则直接返回
     *
     * 如果当前用户为空，则再登录一次，登录成功则返回新的结果，登录失败则抛异常
     */
    suspend fun getUserDataSync(): LoginResult = userData ?: syncLogin().let { result ->
        result.data ?: let { throw java.lang.Exception("以重新登录的方式获取当前用户，重新登录失败${result.exception}") }
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
                            userData = it
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

    suspend fun syncLogin() = withContext(Dispatchers.IO) {
        val userId = LocalUserData.userId
        val result = NetworkRepository.login(userId, LocalUserData.password)
        result.data?.let { loginResult ->
            userData = loginResult
            //登录成功后要进行俩两个任务：同步Bmob数据，还要请求乐健的信息
            val jobBmob = async { asyncBmobData(userId!!, loginResult) }
            val jobLegym = async {
                NetworkRepository.getCurrentSemesterId().data?.let { currentResultBean ->
                    currentData = currentResultBean
                    NetworkRepository.getRunningLimit(RunningLimitRequestBean(currentResultBean.id)).data?.let {
                        runningLimitData = it
                    }
                }
            }
            awaitAll(jobBmob, jobLegym)
        }
        uploadLog("登录状态  $result")
        result
    }

    /**
     * 同步更新Bmob的数据
     */
    suspend fun asyncBmobData(legymId: String, loginResult: LoginResult) =
        withContext(Dispatchers.IO) {
            getBmobDataByLegymId(legymId) ?: let {
                //没有账号就注册
                val newUser = loginResult.generateBmobUser(legymId)
                newUser.suspendSaveSync()
                getBmobDataByLegymId(legymId) ?: throw Exception("Bmob注册新用户后再查找依旧找不到。  $newUser")
            }
        }.apply {
            bmobUser = this
        }


}
