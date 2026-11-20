package ru.nyxsed.orders.model

import android.os.Bundle
import ru.nyxsed.orders.R

enum class GroupLayout {
    VERTICAL,
    HORIZONTAL
}

enum class OrderType(
    val maxPerGroup: Int,
    val iconResId: Int
) {
    REGULAR(15, R.drawable.ic_order_regular),
    IRREGULAR(15, R.drawable.ic_order_irregular),
    IMPETUOUS(15, R.drawable.ic_order_impetous),
    TACTICAL_AWARENESS(15, R.drawable.ic_order_tactical),
    LIEUTENANT(2, R.drawable.ic_order_lieutenant)
}

data class Order(
    val id: Int,
    val type: OrderType,
    val isSpent: Boolean = false
)

data class OrderGroup(
    val id: Int,
    val orders: List<Order> = emptyList()
)

data class CommandToken(
    val id: Int,
    val isSpent: Boolean = false
)

data class AppState(
    val groups: List<OrderGroup>,
    val commandTokens: List<CommandToken>,
    val layout: GroupLayout = GroupLayout.VERTICAL,
    val nextOrderId: Int = 1
) {
    fun toBundle(): Bundle {
        return Bundle().apply {
            putInt(KEY_LAYOUT, layout.ordinal)
            putInt(KEY_NEXT_ORDER_ID, nextOrderId)

            putBooleanArray(KEY_COMMAND_TOKENS, commandTokens.map { it.isSpent }.toBooleanArray())

            groups.forEachIndexed { index, group ->
                putIntArray("g_${index}_ids", group.orders.map { it.id }.toIntArray())
                putIntArray("g_${index}_types", group.orders.map { it.type.ordinal }.toIntArray())
                putBooleanArray("g_${index}_spents", group.orders.map { it.isSpent }.toBooleanArray())
            }
        }
    }

    companion object {
        const val MAX_COMMAND_TOKENS = 5

        private const val KEY_LAYOUT = "key_layout"
        private const val KEY_NEXT_ORDER_ID = "key_next_order_id"
        private const val KEY_COMMAND_TOKENS = "key_command_tokens"

        fun createInitialState(): AppState {
            val initialTokens = (1..MAX_COMMAND_TOKENS).map { CommandToken(id = it, isSpent = false) }
            val group1 = OrderGroup(id = 1, orders = emptyList())
            val group2 = OrderGroup(id = 2, orders = emptyList())
            return AppState(
                groups = listOf(group1, group2),
                commandTokens = initialTokens,
                layout = GroupLayout.VERTICAL,
                nextOrderId = 1
            )
        }

        fun fromBundle(bundle: Bundle): AppState {
            val layoutOrdinal = bundle.getInt(KEY_LAYOUT, GroupLayout.VERTICAL.ordinal)
            val layout = GroupLayout.entries.getOrElse(layoutOrdinal) { GroupLayout.VERTICAL }
            val nextOrderId = bundle.getInt(KEY_NEXT_ORDER_ID, 1)

            val tokenSpents = (bundle.getBooleanArray(KEY_COMMAND_TOKENS) ?: booleanArrayOf())
                .take(MAX_COMMAND_TOKENS)
            val commandTokens = (1..MAX_COMMAND_TOKENS).map { index ->
                CommandToken(
                    id = index,
                    isSpent = tokenSpents.getOrElse(index - 1) { false }
                )
            }

            val groups = (0..1).map { index ->
                val ids = bundle.getIntArray("g_${index}_ids") ?: intArrayOf()
                val types = bundle.getIntArray("g_${index}_types") ?: intArrayOf()
                val spents = bundle.getBooleanArray("g_${index}_spents") ?: booleanArrayOf()

                val orders = ids.indices.map { i ->
                    val typeOrdinal = types.getOrElse(i) { 0 }
                    val type = OrderType.entries.getOrElse(typeOrdinal) { OrderType.REGULAR }
                    val isSpent = spents.getOrElse(i) { false }
                    Order(
                        id = ids[i],
                        type = type,
                        isSpent = isSpent
                    )
                }

                OrderGroup(
                    id = index + 1,
                    orders = orders
                )
            }

            return AppState(
                groups = groups,
                commandTokens = commandTokens,
                layout = layout,
                nextOrderId = nextOrderId
            )
        }
    }
}
