package ldh.ui.run

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import central.stu.fucklegym.R
import com.liangguo.androidkit.app.ToastUtil
import com.liangguo.easyingcontext.EasyingContext.context
import fucklegym.top.entropy.PathGenerator
import fucklegym.top.entropy.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ldh.logic.network.NetworkRepository
import ldh.logic.OnlineData
import ldh.ui.run.logic.RunningPrefUtil
import ldh.ui.run.logic.RunningPrefUtil.DEFAULT_DISTANCE_RANGE_FROM
import ldh.ui.run.logic.RunningPrefUtil.DEFAULT_DISTANCE_RANGE_TO
import ldh.ui.run.logic.RunningType


/**
 * @author ldh
 * 时间: 2022/3/17 13:27
 * 邮箱: 2637614077@qq.com
 */
class RunningViewModel : ViewModel() {

    /**
     * 每次跑步的距离的随机区间
     */
    val distanceRange =
        MutableLiveData(listOf(DEFAULT_DISTANCE_RANGE_FROM, DEFAULT_DISTANCE_RANGE_TO))

    val runningType = MutableLiveData<RunningType>()

    val selectedMap = MutableLiveData<String>()

    var runningMaps = mutableSetOf<String>()

    /**
     * 按钮是否允许点击
     */
    val buttonClickable = MutableLiveData(true)

    init {
        runningType.value = RunningType.getRunningTypeByPrefValue(RunningPrefUtil.prefRunningType)
    }

    /**
     * 更新跑步类型的数据
     */
    fun updateRunningType(type: RunningType) {
        RunningPrefUtil.prefRunningType = type.prefValue
        runningType.value = RunningType.getRunningTypeByPrefValue(RunningPrefUtil.prefRunningType)
    }

    fun selectMap(map: String) {
        RunningPrefUtil.prefRunningMap = map
        updateRunningMap()
    }

    /**
     * 上传跑步数据
     */
    fun uploadRunningData() {
        buttonClickable.value = false
        viewModelScope.launch(Dispatchers.IO) {
            OnlineData.user.let {
                val e = try {
                    NetworkRepository.uploadRunningData(it as User)
                   null
                } catch (e: Exception) { e }

                withContext(Dispatchers.Main) {
                    e?.let {
                        ToastUtil.success(context.getString(R.string.upload_failed))
                        buttonClickable.value = true
                    } ?: ToastUtil.success(context.getString(R.string.upload_success))
                }
            }
        }
    }

    /**
     * 更新一次地图。
     * 包括当前所有的地图数据以及用户偏好的哪一个地图。
     */
    fun updateRunningMap() {
        viewModelScope.launch(Dispatchers.IO) {
            PathGenerator.getLocalMaps(
                context.getSharedPreferences(
                    context.getString(R.string.local_maps_path),
                    Context.MODE_PRIVATE
                )
            )
            runningMaps = PathGenerator.RunMaps.keys
            RunningPrefUtil.prefRunningMap?.let {
                withContext(Dispatchers.Main) {
                    selectedMap.value = it
                }
            }
        }
    }


}