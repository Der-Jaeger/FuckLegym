package ldh.config

import android.app.Application
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.crashreport.CrashReport


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
        Bugly.init(this, "9333bb0d44", true)
        Beta.checkUpgrade(false, true)
    }

}