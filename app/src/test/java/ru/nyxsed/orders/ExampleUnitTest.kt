package ru.nyxsed.orders

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.nyxsed.orders.model.AppState
import ru.nyxsed.orders.model.CommandToken
import ru.nyxsed.orders.model.Order
import ru.nyxsed.orders.model.OrderGroup
import ru.nyxsed.orders.model.OrderType

class ExampleUnitTest {

    @Test
    fun fullReset_clearsOrdersAndSetsFourTokensSpent() {
        val initialTokens = (1..5).map { CommandToken(id = it, isSpent = false) }
        val group1 = OrderGroup(id = 1, orders = listOf(Order(id = 1, type = OrderType.REGULAR)))
        val group2 = OrderGroup(id = 2, orders = listOf(Order(id = 2, type = OrderType.LIEUTENANT)))
        val state = AppState(
            groups = listOf(group1, group2),
            commandTokens = initialTokens,
            nextOrderId = 3
        )

        val resetTokens = (1..AppState.MAX_COMMAND_TOKENS).map { index ->
            CommandToken(id = index, isSpent = index > 1)
        }
        val resetGroups = state.groups.map { group -> group.copy(orders = emptyList()) }
        val resetState = state.copy(
            groups = resetGroups,
            commandTokens = resetTokens,
            nextOrderId = 1
        )

        assertEquals(0, resetState.groups[0].orders.size)
        assertEquals(0, resetState.groups[1].orders.size)

        val spentTokensCount = resetState.commandTokens.count { it.isSpent }
        assertEquals(4, spentTokensCount)
        assertFalse(resetState.commandTokens[0].isSpent)
        assertTrue(resetState.commandTokens[1].isSpent)
        assertTrue(resetState.commandTokens[2].isSpent)
        assertTrue(resetState.commandTokens[3].isSpent)
        assertTrue(resetState.commandTokens[4].isSpent)
    }
}
