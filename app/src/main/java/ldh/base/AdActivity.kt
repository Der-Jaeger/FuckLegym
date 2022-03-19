package ldh.base

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import central.stu.fucklegym.R
import central.stu.fucklegym.databinding.InflateAdLayerBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.liangguo.androidkit.app.contentView
import com.liangguo.androidkit.app.matchParent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception


/**
 * @author ldh
 * 时间: 2022/3/19 15:09
 * 邮箱: 2637614077@qq.com
 */
open class AdActivity : AppCompatActivity() {

    /**
     * 装广告的容器
     */
    private val adViewGroup by lazy {
        FrameLayout(this).matchParent().apply {
//            isInvisible = true
        }
    }

    private val binding by lazy {
        DataBindingUtil.inflate<InflateAdLayerBinding>(
            layoutInflater,
            R.layout.inflate_ad_layer,
            adViewGroup,
            true
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()

        MobileAds.initialize(this) {}


    }

    private fun initViews() {
        try {
            (window.decorView as ViewGroup).addView(adViewGroup, 0)
        } catch (e: Exception) { }
        lifecycleScope.launch {
            delay(500)
            try {
                (binding.root as ViewGroup).apply {
                    for (i in 0..childCount) {
                        getChildAt(0).also { view ->
                            if (view is AdView) {
                                view.loadAd(AdRequest.Builder().build())
                            }
                        }
                    }
                }
            } catch (e: Exception) { }
        }
    }

}