package ldh.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import ldh.logic.OnlineData
import ldh.logic.network.NetworkRepository
import ldh.logic.network.model.running.totalRunning.TotalRunningRequestBean


/**
 * @author ldh
 * 时间: 2022/3/18 21:56
 * 邮箱: 2637614077@qq.com
 */
class MainViewModel : ViewModel() {

    /**
     * 这学期已经跑的数
     */
    val totalRunned = MutableLiveData("")

    /**
     * 这学期总共要跑多少公里
     */
    val semTotalMils = MutableLiveData(0)

    /**
     * 当前已经跑了多少（整数）
     */
    val currentMilsInt = MutableLiveData(0)

    /**
     * 加载已经跑了多少数
     */
    fun loadHasRun() {
        viewModelScope.apply {
            launch(Dispatchers.IO) {
                OnlineData.currentData.let { resultBean->
                    NetworkRepository.getTotalRunning(
                        TotalRunningRequestBean(
                            true,
                            resultBean.id
                        )
                    ).data?.totalMileage?.let { it ->
                        totalRunned.postValue(it)
                        it.toFloatOrNull()?.apply {
                            currentMilsInt.postValue(this.toInt())
                        }
                    }
                }
            }

            launch(Dispatchers.IO) {
                OnlineData.runningLimitData.let {
                    semTotalMils.postValue(it.semesterMileage)
                }
            }

        }
    }

}