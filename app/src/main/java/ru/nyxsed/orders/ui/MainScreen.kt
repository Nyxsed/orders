package ru.nyxsed.orders.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.nyxsed.orders.model.GroupLayout
import ru.nyxsed.orders.ui.components.GroupSection
import ru.nyxsed.orders.ui.components.HelpDialog
import ru.nyxsed.orders.ui.components.TopHeaderBar

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showHelpDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopHeaderBar(
                commandTokens = uiState.commandTokens,
                speedballTokens = uiState.speedballTokens,
                currentLayout = uiState.layout,
                isLossOfLt = uiState.isLossOfLt,
                victoryPoints = uiState.victoryPoints,
                onResetOrders = { viewModel.resetOrders() },
                onFullReset = { viewModel.fullReset() },
                onToggleCommandToken = { viewModel.toggleCommandToken(it) },
                onToggleSpeedballToken = { viewModel.toggleSpeedballToken(it) },
                onSelectLayout = { viewModel.setLayout(it) },
                onToggleLossOfLt = { viewModel.toggleLossOfLt() },
                onChangeVictoryPoints = { viewModel.changeVictoryPoints(it) },
                onOpenHelp = { showHelpDialog = true }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (uiState.layout == GroupLayout.VERTICAL) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    uiState.groups.forEach { group ->
                        GroupSection(
                            group = group,
                            isLossOfLt = uiState.isLossOfLt,
                            onAddOrder = { type -> viewModel.addOrder(group.id, type) },
                            onRemoveOrder = { type -> viewModel.removeOrder(group.id, type) },
                            onToggleOrder = { orderId -> viewModel.toggleOrder(group.id, orderId) }
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.groups.forEach { group ->
                        GroupSection(
                            group = group,
                            isLossOfLt = uiState.isLossOfLt,
                            onAddOrder = { type -> viewModel.addOrder(group.id, type) },
                            onRemoveOrder = { type -> viewModel.removeOrder(group.id, type) },
                            onToggleOrder = { orderId -> viewModel.toggleOrder(group.id, orderId) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        if (showHelpDialog) {
            HelpDialog(
                onDismiss = { showHelpDialog = false }
            )
        }
    }
}
