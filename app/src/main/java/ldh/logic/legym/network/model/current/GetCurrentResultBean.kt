package ldh.logic.legym.network.model.current

data class GetCurrentResultBean(
    val current: Any,
    val endDate: Long,
    val id: String,
    val index: Int,
    val organizationId: String,
    val startDate: Long,
    val weekIndex: Int,
    val yearRange: String
)