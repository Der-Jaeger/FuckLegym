package ldh.logic.clouds.model

data class Notice(
    val buttons: List<Button>,
    val id: String,
    val alwaysShow: Boolean,
    val message: String,
    val show: Boolean,
    val targetActivity: String,
    val title: String
)

