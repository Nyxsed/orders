package ru.nyxsed.orders.ui.theme

import androidx.compose.ui.graphics.Color
import ru.nyxsed.orders.model.OrderType

object OrderColors {
    val RegularThumb = Color(0xFF4CAF50)
    val IrregularThumb = Color(0xFFFFC107)
    val ImpetuousThumb = Color(0xFFF44336)
    val TacticalThumb = Color(0xFF9C27B0)
    val LieutenantThumb = Color(0xFF2196F3)
    val CommandTokenThumb = Color(0xFF00BCD4)
    val SpeedballThumb = Color(0xFFFFB300)

    val TrackReadyBackground = Color(0xFF2E7D32)
    val TrackSpentBackground = Color(0xFFC62828)
    val CommandTokenSpent = Color(0xFF37474F)

    fun getThumbColor(type: OrderType): Color = when (type) {
        OrderType.REGULAR -> RegularThumb
        OrderType.IRREGULAR -> IrregularThumb
        OrderType.IMPETUOUS -> ImpetuousThumb
        OrderType.TACTICAL_AWARENESS -> TacticalThumb
        OrderType.LIEUTENANT -> LieutenantThumb
    }
}
