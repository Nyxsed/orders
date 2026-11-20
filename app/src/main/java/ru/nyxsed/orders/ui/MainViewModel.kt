package ru.nyxsed.orders.ui

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.nyxsed.orders.model.AppState
import ru.nyxsed.orders.model.GroupLayout
import ru.nyxsed.orders.model.Order
import ru.nyxsed.orders.model.OrderType

class MainViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<AppState>
    val uiState: StateFlow<AppState>

    init {
        val bundle: Bundle? = savedStateHandle.get<Bundle>(KEY_STATE)
        val initialState = bundle?.let { AppState.fromBundle(it) } ?: AppState.createInitialState()
        _uiState = MutableStateFlow(initialState)
        uiState = _uiState.asStateFlow()
    }

    private fun updateState(newState: AppState) {
        val sanitizedState = if (newState.commandTokens.size > AppState.MAX_COMMAND_TOKENS) {
            newState.copy(commandTokens = newState.commandTokens.take(AppState.MAX_COMMAND_TOKENS))
        } else {
            newState
        }
        _uiState.value = sanitizedState
        savedStateHandle[KEY_STATE] = sanitizedState.toBundle()
    }

    fun addOrder(groupId: Int, type: OrderType) {
        val currentState = _uiState.value
        var currentNextId = currentState.nextOrderId

        if (type == OrderType.LIEUTENANT) {
            // Лейтенантских приказов может быть 1 или 2, и все они живут только в одной группе.
            val targetGroup = currentState.groups.firstOrNull { it.id == groupId } ?: return
            val ownLieutenants = targetGroup.orders.filter { it.type == OrderType.LIEUTENANT }
            val otherLieutenants = currentState.groups
                .filter { it.id != groupId }
                .flatMap { group -> group.orders.filter { it.type == OrderType.LIEUTENANT } }

            val movedLieutenants = (ownLieutenants + otherLieutenants)
                .take(OrderType.LIEUTENANT.maxPerGroup)
            val lieutenantOrders = if (
                otherLieutenants.isEmpty() &&
                movedLieutenants.size < OrderType.LIEUTENANT.maxPerGroup
            ) {
                // В этой группе уже есть один лейтенант — добавляем второго (или первого).
                movedLieutenants + Order(
                    id = currentNextId++,
                    type = OrderType.LIEUTENANT,
                    isSpent = false
                )
            } else {
                // Приказы уже есть в другой группе — переносим их без изменения количества.
                movedLieutenants
            }

            val updatedGroups = currentState.groups.map { group ->
                group.copy(
                    orders = if (group.id == groupId) {
                        group.orders.filter { it.type != OrderType.LIEUTENANT } + lieutenantOrders
                    } else {
                        group.orders.filter { it.type != OrderType.LIEUTENANT }
                    }
                )
            }

            updateState(
                currentState.copy(
                    groups = updatedGroups,
                    nextOrderId = currentNextId
                )
            )
            return
        }

        val updatedGroups = currentState.groups.map { group ->
            if (group.id == groupId) {
                val count = group.orders.count { it.type == type }
                if (count >= type.maxPerGroup) {
                    group
                } else {
                    val newOrder = Order(id = currentNextId++, type = type, isSpent = false)
                    group.copy(orders = group.orders + newOrder)
                }
            } else {
                group
            }
        }

        updateState(
            currentState.copy(
                groups = updatedGroups,
                nextOrderId = currentNextId
            )
        )
    }

    fun removeOrder(groupId: Int, type: OrderType) {
        val currentState = _uiState.value

        val updatedGroups = currentState.groups.map { group ->
            if (group.id == groupId) {
                val lastIndex = group.orders.indexOfLast { it.type == type }
                if (lastIndex != -1) {
                    val mutableOrders = group.orders.toMutableList()
                    mutableOrders.removeAt(lastIndex)
                    group.copy(orders = mutableOrders)
                } else {
                    group
                }
            } else {
                group
            }
        }

        updateState(currentState.copy(groups = updatedGroups))
    }

    fun toggleOrder(groupId: Int, orderId: Int) {
        val currentState = _uiState.value
        val updatedGroups = currentState.groups.map { group ->
            if (group.id == groupId) {
                val updatedOrders = group.orders.map { order ->
                    if (order.id == orderId) {
                        order.copy(isSpent = !order.isSpent)
                    } else {
                        order
                    }
                }
                group.copy(orders = updatedOrders)
            } else {
                group
            }
        }
        updateState(currentState.copy(groups = updatedGroups))
    }

    fun toggleCommandToken(tokenId: Int) {
        val currentState = _uiState.value
        val updatedTokens = currentState.commandTokens.map { token ->
            if (token.id == tokenId) {
                token.copy(isSpent = !token.isSpent)
            } else {
                token
            }
        }
        updateState(currentState.copy(commandTokens = updatedTokens))
    }

    fun resetOrders() {
        val currentState = _uiState.value
        val updatedGroups = currentState.groups.map { group ->
            val resetOrdersList = group.orders.map { order ->
                order.copy(isSpent = false)
            }
            group.copy(orders = resetOrdersList)
        }
        updateState(currentState.copy(groups = updatedGroups))
    }

    fun setLayout(layout: GroupLayout) {
        val currentState = _uiState.value
        if (currentState.layout != layout) {
            updateState(currentState.copy(layout = layout))
        }
    }

    companion object {
        private const val KEY_STATE = "app_state_bundle"
    }
}
