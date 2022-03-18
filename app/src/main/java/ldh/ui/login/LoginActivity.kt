package ldh.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import central.stu.fucklegym.R
import central.stu.fucklegym.databinding.ActivityLoginBinding
import com.liangguo.androidkit.app.ToastUtil
import com.liangguo.androidkit.app.startNewActivity
import kotlinx.coroutines.launch
import ldh.base.BaseActivity
import ldh.logic.OnlineData
import ldh.ui.login.logic.LocalUserData.password
import ldh.ui.login.logic.LocalUserData.userId
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

    override fun onCreate(savedInstanceState: Bundle?) {
        login {
            initUI()
        }
        super.onCreate(savedInstanceState)
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

            mViewModel.agree.observe(this@LoginActivity) {
                viewModel = mViewModel
            }
        }
    }

    /**
     * 根据本地数据直接进行登录
     */
    private fun login(onFailed: (() -> Unit)? = null) {
        lifecycleScope.launch {
            OnlineData.syncLogin().apply {
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