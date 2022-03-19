package ldh.config

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ldh.logic.clouds.CloudsNetworkRepository
import ldh.utils.LogUtil


/**
 * @author ldh
 * 时间: 2022/3/19 11:54
 * 邮箱: 2637614077@qq.com
 */
object AppLifecycleObserver : LifecycleEventObserver {

    private fun onResume() {
        CoroutineScope(Dispatchers.IO).launch {
            CloudsNetworkRepository.isEnableUploadLog()?.enableUploadLog?.let {
                LogUtil.enableUploadLog = it
            }
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                onResume()
            }
        }
    }

}