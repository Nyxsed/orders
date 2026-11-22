package ru.nyxsed.orders.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import ru.nyxsed.orders.ui.theme.OrderColors

@Composable
fun TopHeaderBar(
    commandTokens: List<CommandToken>,
    currentLayout: GroupLayout,
    onResetOrders: () -> Unit,
    onFullReset: () -> Unit,
    onToggleCommandToken: (Int) -> Unit,
    onSelectLayout: (GroupLayout) -> Unit,
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
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
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
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_reset),
                            contentDescription = "Reset Orders",
                            tint = MaterialTheme.colorScheme.onError,
                            modifier = Modifier
                                .size(18.dp)
                                .graphicsLayer {
                                    rotationZ = rotation.value
                                }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RESET ORDERS",
                            color = MaterialTheme.colorScheme.onError,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
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

            // Command Tokens Block
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                }
            }
        }
    }
}
