package ldh.ui.login

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import central.stu.fucklegym.MainActivity
import central.stu.fucklegym.R
import central.stu.fucklegym.databinding.ActivityLoginBinding
import com.liangguo.androidkit.app.ToastUtil
import com.liangguo.androidkit.app.startNewActivity
import com.tencent.mmkv.MMKV
import fucklegym.top.entropy.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ldh.base.BaseActivity
import ldh.logic.OnlineData.user
import ldh.ui.login.logic.LocalUserData


/**
 * @author ldh
 * 时间: 2022/3/17 21:52
 * 邮箱: 2637614077@qq.com
 */
class LoginActivity : BaseActivity() {

    private val mDataBinding by lazy {
        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        login {
            initViews()
        }
        super.onCreate(savedInstanceState)
    }

    private fun initViews() {
        mDataBinding.apply {
            edit1.setText(LocalUserData.userId)

            edit2.setText(LocalUserData.password)

            buttonLogin.setOnClickListener {
                LocalUserData.userId = edit1.text.toString()
                LocalUserData.password = edit2.text.toString()
                login {
                    ToastUtil.error(getString(R.string.login_failed))
                }
            }
        }
    }

    /**
     * 根据本地数据直接进行登录
     */
    private fun login(onFailed: (() -> Unit)? = null) {
        lifecycleScope.launch(Dispatchers.IO) {
            val e = try {
                user.login()
                null
            } catch (e: Exception) {
                e
            }
            withContext(Dispatchers.Main) {
                e?.let {
                    onFailed?.invoke()
                } ?: let {
                    //直接登录成功，打开MainActivity
                    MainActivity::class.startNewActivity()
                    finish()
                }
            }
        }
    }


}