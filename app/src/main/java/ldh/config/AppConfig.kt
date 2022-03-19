package ldh.config

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import com.liangguo.easyingcontext.EasyingContext.context
import java.text.SimpleDateFormat


/**
 * @author ldh
 * 时间: 2022/3/18 15:37
 * 邮箱: 2637614077@qq.com
 */
object AppConfig {

    /**
     * 乐健体育的版本号
     */
    const val LEGYM_APP_VERSION = "3.1.0"

    @SuppressLint("SimpleDateFormat")
    val legymDateStringFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    /**
     * 应用版本号
     */
    val versionCode: Int
        get() {
            var versionCode = 10
            try {
                versionCode =
                    context.packageManager.getPackageInfo(context.packageName, 0).versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return versionCode
        }


}