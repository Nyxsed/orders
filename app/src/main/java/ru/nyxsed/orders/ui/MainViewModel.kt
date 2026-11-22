package ru.nyxsed.orders.ui

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.nyxsed.orders.model.AppState
import ru.nyxsed.orders.model.CommandToken
import ru.nyxsed.orders.model.GroupLayout
import ru.nyxsed.orders.model.Order
import ru.nyxsed.orders.model.OrderType

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "orders_state_prefs")

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dataStore = application.applicationContext.dataStore
    private val stateKey = stringPreferencesKey("app_state_json")

    private val _uiState: MutableStateFlow<AppState> = MutableStateFlow(AppState.createInitialState())
    val uiState: StateFlow<AppState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val preferences = dataStore.data.first()
                val jsonStr = preferences[stateKey]
                if (jsonStr != null) {
                    val loadedState = AppState.fromJson(jsonStr)
                    if (loadedState != null) {
                        _uiState.value = loadedState
                    }
                }
            } catch (_: Exception) {
                // Fallback to initial state
            }
        }
    }

    private fun updateState(newState: AppState) {
        val sanitizedState = if (newState.commandTokens.size > AppState.MAX_COMMAND_TOKENS) {
            newState.copy(commandTokens = newState.commandTokens.take(AppState.MAX_COMMAND_TOKENS))
        } else {
            newState
        }
        _uiState.value = sanitizedState
        viewModelScope.launch {
            try {
                dataStore.edit { preferences ->
                    preferences[stateKey] = sanitizedState.toJson()
                }
            } catch (_: Exception) {
                // Handle persistence error if needed
            }
        }
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

    fun fullReset() {
        val currentState = _uiState.value
        val resetTokens = (1..AppState.MAX_COMMAND_TOKENS).map { index ->
            CommandToken(
                id = index,
                isSpent = index > 1
            )
        }
        val resetGroups = currentState.groups.map { group ->
            group.copy(orders = emptyList())
        }
        updateState(
            currentState.copy(
                groups = resetGroups,
                commandTokens = resetTokens,
                nextOrderId = 1
            )
        )
    }

    fun setLayout(layout: GroupLayout) {
        val currentState = _uiState.value
        if (currentState.layout != layout) {
            updateState(currentState.copy(layout = layout))
        }
    }
}
