package ldh.ui.login

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import central.stu.fucklegym.R
import central.stu.fucklegym.databinding.ActivityLoginBinding
import com.liangguo.androidkit.app.ToastUtil
import com.liangguo.androidkit.app.startNewActivity
import com.liangguo.androidkit.color.ColorUtil
import com.liangguo.androidkit.color.resolveColor
import com.liangguo.androidkit.commons.smartNotifyValue
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        statusBarOnly {
            fitWindow = false
            light = false
        }
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
        Log.e("测试", "1" + mViewModel.dontPublic.value + "  2" + mViewModel.responsibleSelf.value)
        mDataBinding.buttonLogin.isEnabled =
            (mViewModel.dontPublic.value == true && mViewModel.responsibleSelf.value == true)
    }

    /**
     * 根据本地数据直接进行登录
     */
    private fun login(onFailed: (() -> Unit)? = null) {
        lifecycleScope.launch(Dispatchers.IO) {
            OnlineData.syncLogin().apply {
                Log.e("登录完了", "")
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