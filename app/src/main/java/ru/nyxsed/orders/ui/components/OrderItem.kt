package ru.nyxsed.orders.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.nyxsed.orders.model.Order
import ru.nyxsed.orders.ui.theme.OrderColors

@Composable
fun OrderItem(
    order: Order,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val trackColor by animateColorAsState(
        targetValue = if (order.isSpent) OrderColors.TrackSpentBackground else OrderColors.TrackReadyBackground,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "track_color"
    )

    val progress by animateFloatAsState(
        targetValue = if (order.isSpent) 1f else 0f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "thumb_position"
    )

    val thumbColor = OrderColors.getThumbColor(order.type)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(trackColor)
            .clickable { onToggle() }
            .padding(4.dp)
    ) {
        val totalWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val thumbSizePx = with(LocalDensity.current) { 40.dp.toPx() }
        val maxOffsetPx = totalWidthPx - thumbSizePx
        val currentOffsetPx = maxOffsetPx * progress
        val currentOffsetDp = with(LocalDensity.current) { currentOffsetPx.toDp() }

        Box(
            modifier = Modifier
                .offset(x = currentOffsetDp)
                .size(40.dp)
                .clip(CircleShape)
                .background(thumbColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = order.type.iconResId),
                contentDescription = order.type.name,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
