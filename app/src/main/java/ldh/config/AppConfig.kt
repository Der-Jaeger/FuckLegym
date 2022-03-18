package ldh.config

import android.annotation.SuppressLint
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

}