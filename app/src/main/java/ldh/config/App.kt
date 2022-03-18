package ldh.config

import android.app.Application
import central.stu.fucklegym.BuildConfig
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.mmkv.BuildConfig
import com.tencent.mmkv.MMKV


/**
 * @author ldh
 * 时间: 2022/3/16 21:43
 * 邮箱: 2637614077@qq.com
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initConfig()
    }

    private fun initConfig() {

        //配置Bugly
        Bugly.init(this, "9333bb0d44", BuildConfig.DEBUG)
        Beta.checkUpgrade(false, true)

        MMKV.initialize(this)
    }

}