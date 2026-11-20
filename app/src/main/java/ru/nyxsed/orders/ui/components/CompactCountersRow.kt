package ru.nyxsed.orders.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nyxsed.orders.model.Order
import ru.nyxsed.orders.model.OrderType
import ru.nyxsed.orders.ui.theme.OrderColors

@Composable
fun CompactCountersRow(
    orders: List<Order>,
    onAddOrder: (OrderType) -> Unit,
    onRemoveOrder: (OrderType) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(10.dp),
        tonalElevation = 2.dp
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            OrderType.entries.forEach { type ->
                val count = orders.count { it.type == type }
                val canAdd = count < type.maxPerGroup
                val canRemove = count > 0

                CompactCounterItem(
                    type = type,
                    count = count,
                    canAdd = canAdd,
                    canRemove = canRemove,
                    onAdd = { onAddOrder(type) },
                    onRemove = { onRemoveOrder(type) }
                )
            }
        }
    }
}

@Composable
private fun CompactCounterItem(
    type: OrderType,
    count: Int,
    canAdd: Boolean,
    canRemove: Boolean,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    val typeColor = OrderColors.getThumbColor(type)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        // Minus button
        CounterButton(
            text = "−",
            enabled = canRemove,
            color = typeColor.copy(alpha = 0.8f),
            onClick = onRemove
        )

        // Count
        Text(
            text = "$count",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.widthIn(min = 14.dp)
        )

        // Plus button
        CounterButton(
            text = "+",
            enabled = canAdd,
            color = typeColor,
            onClick = onAdd
        )
    }
}

@Composable
private fun CounterButton(
    text: String,
    enabled: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(
                if (enabled) color else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (enabled) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    }
}
