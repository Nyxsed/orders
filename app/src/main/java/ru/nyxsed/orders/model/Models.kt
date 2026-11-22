package ru.nyxsed.orders.model

import android.os.Bundle
import org.json.JSONArray
import org.json.JSONObject
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
    val nextOrderId: Int = 1,
    val isLossOfLt: Boolean = false,
    val victoryPoints: Int = 0
) {
    fun toBundle(): Bundle {
        return Bundle().apply {
            putInt(KEY_LAYOUT, layout.ordinal)
            putInt(KEY_NEXT_ORDER_ID, nextOrderId)
            putBoolean(KEY_LOSS_OF_LT, isLossOfLt)
            putInt(KEY_VICTORY_POINTS, victoryPoints)

            putBooleanArray(KEY_COMMAND_TOKENS, commandTokens.map { it.isSpent }.toBooleanArray())

            groups.forEachIndexed { index, group ->
                putIntArray("g_${index}_ids", group.orders.map { it.id }.toIntArray())
                putIntArray("g_${index}_types", group.orders.map { it.type.ordinal }.toIntArray())
                putBooleanArray("g_${index}_spents", group.orders.map { it.isSpent }.toBooleanArray())
            }
        }
    }

    fun toJson(): String {
        val json = JSONObject()
        json.put("layout", layout.ordinal)
        json.put("nextOrderId", nextOrderId)
        json.put("isLossOfLt", isLossOfLt)
        json.put("victoryPoints", victoryPoints)

        val tokensArray = JSONArray()
        commandTokens.forEach { token ->
            val tokenObj = JSONObject()
            tokenObj.put("id", token.id)
            tokenObj.put("isSpent", token.isSpent)
            tokensArray.put(tokenObj)
        }
        json.put("commandTokens", tokensArray)

        val groupsArray = JSONArray()
        groups.forEach { group ->
            val groupObj = JSONObject()
            groupObj.put("id", group.id)

            val ordersArray = JSONArray()
            group.orders.forEach { order ->
                val orderObj = JSONObject()
                orderObj.put("id", order.id)
                orderObj.put("type", order.type.ordinal)
                orderObj.put("isSpent", order.isSpent)
                ordersArray.put(orderObj)
            }
            groupObj.put("orders", ordersArray)
            groupsArray.put(groupObj)
        }
        json.put("groups", groupsArray)

        return json.toString()
    }

    companion object {
        const val MAX_COMMAND_TOKENS = 5

        private const val KEY_LAYOUT = "key_layout"
        private const val KEY_NEXT_ORDER_ID = "key_next_order_id"
        private const val KEY_COMMAND_TOKENS = "key_command_tokens"
        private const val KEY_LOSS_OF_LT = "key_loss_of_lt"
        private const val KEY_VICTORY_POINTS = "key_victory_points"

        fun createInitialState(): AppState {
            val initialTokens = (1..MAX_COMMAND_TOKENS).map { CommandToken(id = it, isSpent = false) }
            val group1 = OrderGroup(id = 1, orders = emptyList())
            val group2 = OrderGroup(id = 2, orders = emptyList())
            return AppState(
                groups = listOf(group1, group2),
                commandTokens = initialTokens,
                layout = GroupLayout.VERTICAL,
                nextOrderId = 1,
                isLossOfLt = false,
                victoryPoints = 0
            )
        }

        fun fromJson(jsonStr: String): AppState? {
            try {
                val json = JSONObject(jsonStr)
                val layoutOrdinal = json.optInt("layout", GroupLayout.VERTICAL.ordinal)
                val layout = GroupLayout.entries.getOrElse(layoutOrdinal) { GroupLayout.VERTICAL }
                val nextOrderId = json.optInt("nextOrderId", 1)
                val isLossOfLt = json.optBoolean("isLossOfLt", false)
                val victoryPoints = json.optInt("victoryPoints", 0)

                val tokensArray = json.optJSONArray("commandTokens") ?: JSONArray()
                val commandTokens = (0 until tokensArray.length()).map { i ->
                    val tokenObj = tokensArray.getJSONObject(i)
                    CommandToken(
                        id = tokenObj.optInt("id", i + 1),
                        isSpent = tokenObj.optBoolean("isSpent", false)
                    )
                }.take(MAX_COMMAND_TOKENS).let { list ->
                    if (list.isEmpty()) {
                        (1..MAX_COMMAND_TOKENS).map { CommandToken(id = it, isSpent = false) }
                    } else {
                        list
                    }
                }

                val groupsArray = json.optJSONArray("groups") ?: JSONArray()
                val groups = (0 until groupsArray.length()).map { i ->
                    val groupObj = groupsArray.getJSONObject(i)
                    val groupId = groupObj.optInt("id", i + 1)

                    val ordersArray = groupObj.optJSONArray("orders") ?: JSONArray()
                    val orders = (0 until ordersArray.length()).map { j ->
                        val orderObj = ordersArray.getJSONObject(j)
                        val typeOrdinal = orderObj.optInt("type", 0)
                        val type = OrderType.entries.getOrElse(typeOrdinal) { OrderType.REGULAR }
                        Order(
                            id = orderObj.optInt("id", j + 1),
                            type = type,
                            isSpent = orderObj.optBoolean("isSpent", false)
                        )
                    }

                    OrderGroup(
                        id = groupId,
                        orders = orders
                    )
                }.let { list ->
                    if (list.isEmpty()) {
                        listOf(OrderGroup(id = 1), OrderGroup(id = 2))
                    } else {
                        list
                    }
                }

                return AppState(
                    groups = groups,
                    commandTokens = commandTokens,
                    layout = layout,
                    nextOrderId = nextOrderId,
                    isLossOfLt = isLossOfLt,
                    victoryPoints = victoryPoints
                )
            } catch (e: Exception) {
                return null
            }
        }

        fun fromBundle(bundle: Bundle): AppState {
            val layoutOrdinal = bundle.getInt(KEY_LAYOUT, GroupLayout.VERTICAL.ordinal)
            val layout = GroupLayout.entries.getOrElse(layoutOrdinal) { GroupLayout.VERTICAL }
            val nextOrderId = bundle.getInt(KEY_NEXT_ORDER_ID, 1)
            val isLossOfLt = bundle.getBoolean(KEY_LOSS_OF_LT, false)
            val victoryPoints = bundle.getInt(KEY_VICTORY_POINTS, 0)

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
                nextOrderId = nextOrderId,
                isLossOfLt = isLossOfLt,
                victoryPoints = victoryPoints
            )
        }
    }
}
