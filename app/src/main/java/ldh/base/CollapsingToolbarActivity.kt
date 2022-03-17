package ldh.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import central.stu.fucklegym.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.liangguo.androidkit.color.ColorUtil
import com.liangguo.androidkit.color.resolveColor
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly


/**
 * @author ldh
 * 时间: 2022/2/20 12:56
 * 邮箱: 2637614077@qq.com
 */
@SuppressLint("InflateParams")
abstract class CollapsingToolbarActivity : BaseActivity() {

    /**
     * 布局的Id，
     * 子类布局可以自定义，不过要保证父类基本的布局元素都在，不然这些View会找不到
     */
    open val layoutResID: Int = R.layout.activity_collapsing_toolbar

    /**
     * 这个Activity的ContentView里塞的真正的东西，也是相当于这个Activity的布局文件转化出来的根布局
     */
    protected open val rootLayout by lazy {
        LayoutInflater.from(this).inflate(layoutResID, null) as ViewGroup
    }

    protected val toolbar: MaterialToolbar by lazy {
        findViewById(R.id.toolbar)
    }

    protected val collapsingToolbarLayout: CollapsingToolbarLayout by lazy {
        findViewById(R.id.collapsingToolbarLayout)
    }

    protected val appbarLayout: AppBarLayout by lazy {
        findViewById(R.id.appBarLayout)
    }

    /**
     * 子类用来装ContentView的容器
     */
    protected open val contentViewContainer: ViewGroup by lazy {
        rootLayout.findViewById(R.id.contentViewContainer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        require(actionBar == null && supportActionBar == null) {
            "使用${javaClass.simpleName}时，应该搭配没有ActionBar的主题"
        }
        super.setContentView(rootLayout)
        setSupportActionBar(toolbar)
        //默认显示返回键，子类不要的话自己调用这个方法设置
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    override fun setContentView(layoutResID: Int) {
        LayoutInflater.from(this).inflate(layoutResID, contentViewContainer, true)
    }

    override fun setContentView(view: View?) {
        contentViewContainer.addView(view)
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        contentViewContainer.addView(view, params)
    }


}