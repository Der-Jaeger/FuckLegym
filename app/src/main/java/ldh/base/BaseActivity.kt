package ldh.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import central.stu.fucklegym.R
import com.liangguo.androidkit.app.contentView
import com.liangguo.androidkit.color.ColorUtil
import com.liangguo.androidkit.color.resolveColor
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly


/**
 * @author ldh
 * 时间: 2022/3/17 16:54
 * 邮箱: 2637614077@qq.com
 */
abstract class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}