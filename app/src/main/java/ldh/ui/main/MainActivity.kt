package ldh.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import central.stu.fucklegym.CourseSignUpActivity
import central.stu.fucklegym.R
import central.stu.fucklegym.SignUp
import com.liangguo.androidkit.app.startNewActivity
import ldh.base.BaseActivity
import ldh.logic.OnlineData
import ldh.ui.login.logic.LocalUserData
import ldh.ui.run.RunningActivity


/**
 * @author ldh
 * 时间: 2022/3/18 11:30
 * 邮箱: 2637614077@qq.com
 */
class MainActivity : BaseActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<central.stu.fucklegym.databinding.ActivityMainBinding>(
            this,
            R.layout.activity_main
        )
    }

    private val mViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initDataListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun initDataListeners() {
        mViewModel.semTotalMils.observe(this) {
            binding.textSemesterMileageTotal.text = "/" + it + "km"
            binding.progress.max = it
        }

        mViewModel.totalRunned.observe(this) {
            binding.textSemesterMileageCurrent.text = it
        }

        mViewModel.currentMilsInt.observe(this) {
            binding.progress.setProgressCompat(it, true)
        }

    }

    private fun initViews() {
        binding.apply {
            setSupportActionBar(toolbar)

            buttonRunning.setOnClickListener {
                RunningActivity::class.startNewActivity()
            }

            buttonSignActivity.setOnClickListener {
                CourseSignUpActivity::class.startNewActivity()
            }

            buttonSignCourse.setOnClickListener {
                SignUp::class.startNewActivity()
            }


            user = OnlineData.userData
            imageHeader.setImageResource(
                if (OnlineData.userData.avatar == 1) R.drawable.ic_avatar_male
                else R.drawable.icon_avatar_man
            )

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        mViewModel.loadHasRun()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out -> {
                LocalUserData.password = ""
                OnlineData.loginAndDo()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}