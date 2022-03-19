package ldh.utils

import android.util.Log
import com.liangguo.easyingcontext.EasyingContext
import com.pgyersdk.crash.PgyCrashManager
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.BuglyLog
import com.tencent.bugly.crashreport.CrashReport


/**
 * @author ldh
 * 时间: 2022/3/19 11:49
 * 邮箱: 2637614077@qq.com
 */
object LogUtil {

    var enableUploadLog = false

    fun uploadLog(data: Any) {
        if (!enableUploadLog) return
        try {
            if (data is java.lang.Exception) {
                CrashReport.postCatchedException(data)
            } else CrashReport.postCatchedException(UploadLog(data.toString()))

            PgyCrashManager.reportCaughtException(
                EasyingContext.context,
                UploadLog(data.toString())
            )

        } catch (e: Exception) {
            Log.e("上传日志失败", e.toString())
        }
    }

    class UploadLog(message: String): Exception(message)

}