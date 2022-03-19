package ldh.logic.clouds.model

data class StopConfig(
    val button: Button?,
    val deprecatedVersion: Int,
    val info: Info,
    val saveVersion: List<Int>
)

/**
 * 用于显示对话框的信息
 */
data class Info(
    val message: String,
    val title: String
)