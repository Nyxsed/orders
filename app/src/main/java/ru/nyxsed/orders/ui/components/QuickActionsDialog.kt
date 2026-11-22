package ru.nyxsed.orders.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ru.nyxsed.orders.R
import ru.nyxsed.orders.model.AppState
import ru.nyxsed.orders.model.OrderType
import ru.nyxsed.orders.ui.theme.OrderColors

@Composable
fun QuickActionsDialog(
    appState: AppState,
    onDismiss: () -> Unit,
    onConvertIrregular: (groupId: Int) -> Unit,
    onCancelLossOfLt: () -> Unit,
    onCallSpeedball: () -> Unit,
    onMoveOrder: (fromGroupId: Int, toGroupId: Int) -> Unit
) {
    val unspentTokensCount = appState.commandTokens.count { !it.isSpent }
    var selectedTab by remember { mutableStateOf(0) } // 0: Quick Actions, 1: Speedball Hint

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_command_token),
                            contentDescription = null,
                            tint = OrderColors.CommandTokenThumb,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Command Triggers",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Token Counter Badge
                    Surface(
                        color = if (unspentTokensCount > 0) OrderColors.CommandTokenThumb else OrderColors.CommandTokenSpent,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Жетоны: $unspentTokensCount/${AppState.MAX_COMMAND_TOKENS}",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Tab Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { selectedTab = 0 },
                        modifier = Modifier.weight(1f),
                        colors = if (selectedTab == 0) ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ) else ButtonDefaults.outlinedButtonColors()
                    ) {
                        Text("⚡ Действия", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { selectedTab = 1 },
                        modifier = Modifier.weight(1f),
                        colors = if (selectedTab == 1) ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ) else ButtonDefaults.outlinedButtonColors()
                    ) {
                        Text("📦 Speedball Drop", fontSize = 12.sp)
                    }
                }

                if (selectedTab == 0) {
                    // Quick Actions Tab
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 1. Convert Irregular -> Regular
                        val hasIrregularG1 = appState.groups.find { it.id == 1 }?.orders?.any { it.type == OrderType.IRREGULAR } == true
                        val hasIrregularG2 = appState.groups.find { it.id == 2 }?.orders?.any { it.type == OrderType.IRREGULAR } == true

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "🔄 Irregular ➔ Regular (+1 жетон)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Преобразует 1 нерегулярный приказ в регулярный",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { onConvertIrregular(1) },
                                        enabled = unspentTokensCount > 0 && hasIrregularG1 && !appState.isLossOfLt,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Группа 1", fontSize = 11.sp)
                                    }
                                    Button(
                                        onClick = { onConvertIrregular(2) },
                                        enabled = unspentTokensCount > 0 && hasIrregularG2 && !appState.isLossOfLt,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Группа 2", fontSize = 11.sp)
                                    }
                                }
                            }
                        }

                        // 2. Appoint New Lieutenant (Cancel Loss of Lt)
                        if (appState.isLossOfLt) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "👑 Назначить нового Лейтенанта",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                    Text(
                                        text = "Снимает состояние Потери Лейтенанта за 1 жетон",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                    Button(
                                        onClick = onCancelLossOfLt,
                                        enabled = unspentTokensCount > 0,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.error
                                        )
                                    ) {
                                        Text("Снять Loss of LT (-1 жетон)")
                                    }
                                }
                            }
                        }

                        // 3. Move Order Between Groups
                        val g1HasOrders = appState.groups.find { it.id == 1 }?.orders?.isNotEmpty() == true
                        val g2HasOrders = appState.groups.find { it.id == 2 }?.orders?.isNotEmpty() == true

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "🔀 Перенос приказа между группами",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedButton(
                                        onClick = { onMoveOrder(1, 2) },
                                        enabled = unspentTokensCount > 0 && g1HasOrders,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Гр. 1 ➔ Гр. 2", fontSize = 11.sp)
                                    }
                                    OutlinedButton(
                                        onClick = { onMoveOrder(2, 1) },
                                        enabled = unspentTokensCount > 0 && g2HasOrders,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Гр. 2 ➔ Гр. 1", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Speedball Drop Tab with Hint Table
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Таблица вызова Speedball / Supply Drop",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "При вызове сбрасывается контейнер припасов. Бросьте 1d6 (или d20):",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Drop Table Card
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SpeedballTableRow(
                                    dice = "d6: 1-2 (d20: 1-6)",
                                    icon = "💨",
                                    title = "Smoke / Eclipse Pod",
                                    desc = "Дымовая или Затмевающая граната"
                                )
                                SpeedballTableRow(
                                    dice = "d6: 3-4 (d20: 7-13)",
                                    icon = "💉",
                                    title = "Med-Kit Servo",
                                    desc = "Автоматический медкит / лечебный пак"
                                )
                                SpeedballTableRow(
                                    dice = "d6: 5-6 (d20: 14-20)",
                                    icon = "💣",
                                    title = "Heavy Ammo Pack",
                                    desc = "Саплай / Патроны для тяжелого оружия"
                                )
                            }
                        }

                        Button(
                            onClick = {
                                onCallSpeedball()
                            },
                            enabled = unspentTokensCount > 0,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = OrderColors.CommandTokenThumb
                            )
                        ) {
                            Text("📦 Вызвать Speedball (-1 жетон)")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Close Button
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Закрыть")
                }
            }
        }
    }
}

@Composable
private fun SpeedballTableRow(
    dice: String,
    icon: String,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(dice, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Text(desc, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
