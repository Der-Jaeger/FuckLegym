package ldh.ui.run

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import central.stu.fucklegym.OnlineMaps
import central.stu.fucklegym.R
import central.stu.fucklegym.databinding.ActivityRunningBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.RangeSlider
import com.liangguo.androidkit.app.startNewActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ldh.base.CollapsingToolbarActivity
import ldh.ui.run.logic.RunningPrefUtil
import ldh.ui.run.logic.RunningType


/**
 * @author ldh
 * 时间: 2022/3/17 11:25
 * 邮箱: 2637614077@qq.com
 */
class RunningActivity : CollapsingToolbarActivity() {

    private val mDataBinding by lazy {
        ActivityRunningBinding.inflate(layoutInflater, contentViewContainer, true)
    }

    private val mViewModel by viewModels<RunningViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initDataListeners()
        initData()
    }

    private fun initData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val range = RunningPrefUtil.prefDistanceRange
            withContext(Dispatchers.Main) {
                mDataBinding.rangeSlider.values = range
            }
        }
    }

    private fun initDataListeners() {
        mViewModel.distanceRange.observe(this) {
            mDataBinding.textSliderValue.text = RANGE_FORMAT.format(it.first(), it.last())
        }
        mViewModel.runningType.observe(this) {
            mDataBinding.settingChooseType.subtitleTextView.text = it.title
        }
        mViewModel.selectedMap.observe(this) {
            mDataBinding.settingChooseArea.subtitleTextView.text = it
        }
        mViewModel.buttonClickable.observe(this) {
            mDataBinding.buttonUpload.isEnabled = it
        }
    }

    private fun initViews() {

        //选择跑步路程范围
        mDataBinding.rangeSlider.addOnChangeListener { slider, _, _ ->
            mViewModel.distanceRange.value = slider.values
        }
        mDataBinding.rangeSlider.addOnSliderTouchListener(object :
            RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {}
            override fun onStopTrackingTouch(slider: RangeSlider) {
                RunningPrefUtil.prefDistanceRange =
                    listOf(slider.values.first(), slider.values.last())
            }
        })

        //选择跑步类型
        mDataBinding.settingChooseType.setOnClickListener {
            val titles = mutableListOf<String>()
            RunningType.allTypes.forEach {
                titles.add(it.title)
            }
            MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Material3)
                .setTitle(R.string.running_type)
                .setItems(titles.toTypedArray()) { _, witch ->
                    mViewModel.updateRunningType(RunningType.allTypes[witch])
                }
                .show()
        }

        //选择跑步的地图
        mDataBinding.settingChooseArea.setOnClickListener {
            val maps = mViewModel.runningMaps.toTypedArray()
            MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Material3)
                .setTitle(R.string.running_area)
                .setPositiveButton(R.string.import_clould_maps) { _, _ ->
                    OnlineMaps::class.startNewActivity()
                }
                .setItems(maps) { _, witch ->
                    mViewModel.selectMap(maps[witch])
                }
                .show()
        }

        //点击上传数据
        mDataBinding.buttonUpload.setOnClickListener {
            mViewModel.uploadRunningData()
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.updateRunningMap()
    }

    companion object {
        private const val RANGE_FORMAT = "从:%.2f\n到:%.2f"
    }

}