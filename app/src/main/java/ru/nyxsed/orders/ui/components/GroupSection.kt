package ru.nyxsed.orders.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.nyxsed.orders.model.Order
import ru.nyxsed.orders.model.OrderGroup
import ru.nyxsed.orders.model.OrderType

@Composable
fun GroupSection(
    group: OrderGroup,
    onAddOrder: (OrderType) -> Unit,
    onRemoveOrder: (OrderType) -> Unit,
    onToggleOrder: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant
            ),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Compact Counters Row
            CompactCountersRow(
                orders = group.orders,
                onAddOrder = onAddOrder,
                onRemoveOrder = onRemoveOrder
            )

            // Orders List ordered by OrderType
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Group orders sequentially by type
                val sortedOrders = OrderType.entries.flatMap { type ->
                    group.orders.filter { it.type == type }
                }

                sortedOrders.forEach { order ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        OrderItem(
                            order = order,
                            onToggle = { onToggleOrder(order.id) }
                        )
                    }
                }
            }
        }
    }
}
