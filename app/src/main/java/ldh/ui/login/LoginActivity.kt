package ldh.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import central.stu.fucklegym.R
import central.stu.fucklegym.databinding.ActivityLoginBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.liangguo.androidkit.app.ToastUtil
import com.liangguo.androidkit.app.startNewActivity
import com.liangguo.androidkit.commons.smartNotifyValue
import com.pgyersdk.crash.PgyCrashManager
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly
import kotlinx.coroutines.*
import ldh.base.BaseActivity
import ldh.config.AppConfig.versionCode
import ldh.logic.OnlineData
import ldh.logic.clouds.CloudsNetworkRepository
import ldh.logic.clouds.model.StopConfig
import ldh.logic.legym.LocalUserData.password
import ldh.logic.legym.LocalUserData.userId
import ldh.ui.main.MainActivity


/**
 * @author ldh
 * 时间: 2022/3/17 21:52
 * 邮箱: 2637614077@qq.com
 */
class LoginActivity : BaseActivity() {

    private val mDataBinding by lazy {
        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
    }

    private val mViewModel by viewModels<LoginViewModel>()

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        initNetwork()
        appCompatOnCreate(savedInstanceState)
        statusBarOnly {
            fitWindow = false
            light = false
        }
    }

    /**
     * 初始化网络模块，这里主要是这些内容：
     *
     * - 根据本地账号密码进行登录并初化用户信息。
     * - 获取的版本停用信息，看看当前版本是否已经被停用了，若被停用，登录功能直接失效。
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun initNetwork() {
        lifecycleScope.launch(Dispatchers.IO) {
            val loginResultJob = async { OnlineData.syncLogin() }
            val stopConfigJob = async { CloudsNetworkRepository.getStopConfig() }
            awaitAll(loginResultJob, stopConfigJob)
            stopConfigJob.getCompleted()?.apply {
                Log.e("停用信息", toString())
                //处理停用信息
                if (deprecatedVersion >= versionCode) {
                    //该版本可能停用，在saveVersions里面找找该版本是否需要保留
                    if (!saveVersion.contains(versionCode)) {
                        //该版本停用，显示停用对话框
                        withContext(Dispatchers.Main) { showStopInfo(this@apply) }
                        return@launch
                    }
                }
            }
            loginResultJob.getCompleted().apply {
                withContext(Dispatchers.Main) {
                    data?.let {
                        MainActivity::class.startNewActivity()
                        finish()
                    } ?: let {
                        PgyCrashManager.reportCaughtException(this@LoginActivity, exception)
                        //登录失败，初始化UI
                        initUI()
                    }
                }

            }
        }
    }

    /**
     * 停用
     */
    private fun showStopInfo(stopConfig: StopConfig) {
        MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Material3)
            .setTitle(stopConfig.info.title)
            .setMessage(stopConfig.info.message)
            .apply {
                stopConfig.button?.let { button ->
                    setPositiveButton(button.text) { _, _ ->
                        button.url?.let {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(it)
                            startActivity(intent)
                        }
                    }
                }
            }
            .setCancelable(false)
            .show()
    }

    private fun initUI() {
        mDataBinding.apply {
            edit1.setText(userId)
            edit2.setText(password)

            buttonLogin.setOnClickListener {
                userId = edit1.text.toString()
                password = edit2.text.toString()
                login {
                    ToastUtil.error(getString(R.string.login_failed))
                }
            }

            mViewModel.dontPublic.observe(this@LoginActivity) {
                checkbox1.isChecked = it
                checkButtonEnable()
            }

            mViewModel.responsibleSelf.observe(this@LoginActivity) {
                checkbox2.isChecked = it
                checkButtonEnable()
            }

            checkbox1.setOnCheckedChangeListener { _, checked ->
                mViewModel.dontPublic.smartNotifyValue = checked
            }

            checkbox2.setOnCheckedChangeListener { _, checked ->
                mViewModel.responsibleSelf.smartNotifyValue = checked
            }
        }
    }

    private fun checkButtonEnable() {
        mDataBinding.buttonLogin.isEnabled =
            (mViewModel.dontPublic.value == true && mViewModel.responsibleSelf.value == true)
    }

    /**
     * 根据本地数据直接进行登录
     */
    private fun login(onFailed: (() -> Unit)? = null) {
        lifecycleScope.launch(Dispatchers.IO) {
            OnlineData.syncLogin().apply {
                withContext(Dispatchers.Main) {
                    exception?.let {
                        onFailed?.invoke()
                    }
                    data?.let {
                        MainActivity::class.startNewActivity()
                        finish()
                    }
                }
            }
        }
    }


}