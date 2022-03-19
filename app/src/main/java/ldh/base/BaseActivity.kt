package ldh.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import central.stu.fucklegym.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.liangguo.androidkit.app.contentView
import com.liangguo.androidkit.color.ColorUtil
import com.liangguo.androidkit.color.resolveColor
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly
import kotlinx.coroutines.launch
import ldh.config.defaultMMKV
import ldh.logic.LocalUserData
import ldh.logic.clouds.CloudsNetworkRepository
import ldh.logic.clouds.model.Button
import ldh.logic.clouds.model.Notice


/**
 * @author ldh
 * 时间: 2022/3/17 16:54
 * 邮箱: 2637614077@qq.com
 */
abstract class BaseActivity : AppCompatActivity() {

    protected val noticeKey: String
        get() = "NoticeKey - " + javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        initNotices()
        super.onCreate(savedInstanceState)

        statusBarOnly {
            //布局是否侵入状态栏（true 不侵入，false 侵入）
            fitWindow = false
            light = ColorUtil.isColorLight(resolveColor(R.attr.colorSurface))
        }
        window.decorView.post {
            contentView?.setBackgroundColor(resolveColor(R.attr.colorSurface))
        }

    }

    protected open fun initNotices() {
        lifecycleScope.launch {
            CloudsNetworkRepository.getNotices()?.forEach { notice ->
                Log.e("通知", notice.toString())
                if (notice.show && notice.targetActivity == this@BaseActivity.javaClass.simpleName) {
                    if (notice.alwaysShow || defaultMMKV.getString(noticeKey, null) != notice.id) {
                        //需要通知
                        onShowNotice(notice)
                        defaultMMKV.putString(noticeKey, notice.id)
                    }
                }
            }
        }
    }

    protected open fun onShowNotice(notice: Notice) {
        with(notice) {
            MaterialAlertDialogBuilder(this@BaseActivity, R.style.MaterialAlertDialog_Material3)
                .setTitle(title)
                .setMessage(message)
                .apply {
                    buttons.forEachIndexed { index, button ->
                        when (index) {
                            0 -> setPositiveButton(button.text) { _, _ ->
                                onNoticeButtonClick(button)
                            }

                            1 -> setNegativeButton(button.text) { _, _ ->
                                onNoticeButtonClick(button)
                            }

                            2 -> setNeutralButton(button.text) { _, _ ->
                                onNoticeButtonClick(button)
                            }
                        }
                    }
                }
                .show()
        }
    }

    protected open fun onNoticeButtonClick(button: Button) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(button.url)
        startActivity(intent)
    }

    protected fun appCompatOnCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}