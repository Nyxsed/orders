package ru.nyxsed.orders.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.nyxsed.orders.R
import ru.nyxsed.orders.model.CommandToken
import ru.nyxsed.orders.model.GroupLayout
import ru.nyxsed.orders.model.SpeedballToken
import ru.nyxsed.orders.ui.theme.OrderColors

@Composable
fun TopHeaderBar(
    commandTokens: List<CommandToken>,
    speedballTokens: List<SpeedballToken>,
    currentLayout: GroupLayout,
    isLossOfLt: Boolean,
    victoryPoints: Int,
    onResetOrders: () -> Unit,
    onFullReset: () -> Unit,
    onToggleCommandToken: (Int) -> Unit,
    onToggleSpeedballToken: (Int) -> Unit,
    onSelectLayout: (GroupLayout) -> Unit,
    onToggleLossOfLt: () -> Unit,
    onChangeVictoryPoints: (Int) -> Unit,
    onOpenQuickActions: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val rotation = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Top Controls Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Animated Reset Button supporting Tap & Long Press
                Surface(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale.value
                            scaleY = scale.value
                        }
                        .clip(RoundedCornerShape(8.dp))
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    coroutineScope.launch {
                                        rotation.animateTo(
                                            targetValue = rotation.value - 180f,
                                            animationSpec = tween(250, easing = FastOutSlowInEasing)
                                        )
                                    }
                                    onResetOrders()
                                },
                                onLongPress = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    coroutineScope.launch {
                                        launch {
                                            scale.animateTo(1.2f, animationSpec = tween(120))
                                            scale.animateTo(
                                                1.0f,
                                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                                            )
                                        }
                                        launch {
                                            rotation.animateTo(
                                                targetValue = rotation.value + 720f,
                                                animationSpec = tween(600, easing = FastOutSlowInEasing)
                                            )
                                        }
                                    }
                                    onFullReset()
                                }
                            )
                        },
                    color = MaterialTheme.colorScheme.error,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_reset),
                            contentDescription = "Reset Orders",
                            tint = MaterialTheme.colorScheme.onError,
                            modifier = Modifier
                                .size(16.dp)
                                .graphicsLayer {
                                    rotationZ = rotation.value
                                }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RESET ORDERS",
                            color = MaterialTheme.colorScheme.onError,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onSelectLayout(GroupLayout.VERTICAL) }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_layout_vertical),
                            contentDescription = "Vertical Layout",
                            tint = if (currentLayout == GroupLayout.VERTICAL) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            }
                        )
                    }

                    IconButton(
                        onClick = { onSelectLayout(GroupLayout.HORIZONTAL) }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_layout_horizontal),
                            contentDescription = "Horizontal Layout",
                            tint = if (currentLayout == GroupLayout.HORIZONTAL) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            }
                        )
                    }
                }
            }

            // Status & VP Counter Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Victory Points Counter
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(10.dp)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "🏆 VP",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // Minus button
                        VpButton(
                            text = "−",
                            enabled = victoryPoints > 0,
                            onClick = { onChangeVictoryPoints(-1) }
                        )

                        Text(
                            text = "$victoryPoints",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )

                        // Plus button
                        VpButton(
                            text = "+",
                            enabled = victoryPoints < 20,
                            onClick = { onChangeVictoryPoints(1) }
                        )
                    }
                }

                // Loss of LT Checkbox Chip
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isLossOfLt) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            width = 1.dp,
                            color = if (isLossOfLt) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { onToggleLossOfLt() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Checkbox(
                            checked = isLossOfLt,
                            onCheckedChange = { onToggleLossOfLt() },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.error,
                                uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = if (isLossOfLt) "⚠️ LOSS OF LT" else "Loss of LT",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (isLossOfLt) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Quick Triggers Button
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = OrderColors.CommandTokenThumb,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onOpenQuickActions() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚡ Triggers",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }

            // Command Tokens & Speedball Tokens Block
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Standard Command Tokens
                    commandTokens.forEach { token ->
                        val tokenColor by animateColorAsState(
                            targetValue = if (token.isSpent) {
                                OrderColors.CommandTokenSpent
                            } else {
                                OrderColors.CommandTokenThumb
                            },
                            animationSpec = tween(durationMillis = 300),
                            label = "token_color"
                        )

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(tokenColor)
                                .clickable { onToggleCommandToken(token.id) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_command_token),
                                contentDescription = "Command Token ${token.id}",
                                tint = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Speedball Chest Tokens (if any)
                    speedballTokens.forEach { speedball ->
                        val speedballColor by animateColorAsState(
                            targetValue = if (speedball.isSpent) {
                                OrderColors.CommandTokenSpent
                            } else {
                                OrderColors.SpeedballThumb
                            },
                            animationSpec = tween(durationMillis = 300),
                            label = "speedball_color"
                        )

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(speedballColor)
                                .clickable { onToggleSpeedballToken(speedball.id) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "📦",
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VpButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(
                if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
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
