package ldh.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import central.stu.fucklegym.R
import central.stu.fucklegym.databinding.ActivityMainBinding
import com.liangguo.androidkit.app.startNewActivity
import ldh.base.BaseActivity
import ldh.logic.OnlineData
import ldh.ui.run.RunningActivity


/**
 * @author ldh
 * 时间: 2022/3/18 11:30
 * 邮箱: 2637614077@qq.com
 */
class MainActivity: BaseActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        OnlineData.userData.observe(this) {
            binding.user = it
        }
        binding.button.setOnClickListener {
            RunningActivity::class.startNewActivity()
        }
    }

}