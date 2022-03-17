package ldh.ui.run.logic

import central.stu.fucklegym.R
import com.liangguo.easyingcontext.EasyingContext.context
import com.liangguo.preference.utils.PreferenceUtil

/**
 * @author ldh
 * 时间: 2022/3/17 14:57
 * 邮箱: 2637614077@qq.com
 */
object RunningPrefUtil {

    /**
     * 默认的距离，目前定为 2.5~4 km
     */
    const val DEFAULT_DISTANCE_RANGE_FROM = 2.5f

    const val DEFAULT_DISTANCE_RANGE_TO = 4f


    /**
     * 通过保存Preference保存在本地的距离范围。
     *
     * 使用该属性get和set都是直接操作Preference。
     */
    var prefDistanceRange: List<Float>
        get() = listOf(
            PreferenceUtil.getFloat(
                R.string.pref_key_distance_range_from,
                DEFAULT_DISTANCE_RANGE_FROM
            ),
            PreferenceUtil.getFloat(
                R.string.pref_key_distance_range_to,
                DEFAULT_DISTANCE_RANGE_TO
            )
        )
        set(value) {
            PreferenceUtil.setFloat(R.string.pref_key_distance_range_from, value.first())
            PreferenceUtil.setFloat(R.string.pref_key_distance_range_to, value.last())
        }

    /**
     * 通过保存Preference保存在本地的距离范围。
     *
     * 使用该属性get和set都是直接操作Preference。
     *
     * 默认为自由跑。
     */
    var prefRunningType: String
        get() = PreferenceUtil.getString(
            R.string.pref_key_running_type,
            RunningType.Free.prefValue
        )
        set(value) {
            PreferenceUtil.setString(R.string.pref_key_running_type, value)
        }

    /**
     * 用户偏好的是哪一个地图。
     */
    var prefRunningMap: String?
        get() = PreferenceUtil.getString(
            R.string.pref_key_running_map,
            context.getString(R.string.pref_key_running_map_uest_chengdu)
        )
        set(value) {
            PreferenceUtil.setString(R.string.pref_key_running_map, value)
        }

}