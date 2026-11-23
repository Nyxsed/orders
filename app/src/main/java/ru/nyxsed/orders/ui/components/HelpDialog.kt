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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ru.nyxsed.orders.R

@Composable
fun HelpDialog(
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Cmd Tokens, 1: Speedball, 2: Booty, 3: Metachemistry

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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Dialog Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_command_token),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.help_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Tab Selectors
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedButton(
                            onClick = { selectedTab = 0 },
                            modifier = Modifier.weight(1f),
                            colors = if (selectedTab == 0) ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ) else ButtonDefaults.outlinedButtonColors()
                        ) {
                            Text(
                                text = stringResource(R.string.help_tab_command_tokens),
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        }

                        OutlinedButton(
                            onClick = { selectedTab = 1 },
                            modifier = Modifier.weight(1f),
                            colors = if (selectedTab == 1) ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ) else ButtonDefaults.outlinedButtonColors()
                        ) {
                            Text(
                                text = stringResource(R.string.help_tab_speedball),
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedButton(
                            onClick = { selectedTab = 2 },
                            modifier = Modifier.weight(1f),
                            colors = if (selectedTab == 2) ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ) else ButtonDefaults.outlinedButtonColors()
                        ) {
                            Text(
                                text = stringResource(R.string.help_tab_booty),
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        }

                        OutlinedButton(
                            onClick = { selectedTab = 3 },
                            modifier = Modifier.weight(1f),
                            colors = if (selectedTab == 3) ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ) else ButtonDefaults.outlinedButtonColors()
                        ) {
                            Text(
                                text = stringResource(R.string.help_tab_metachemistry),
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))

                // Scrollable Content Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    when (selectedTab) {
                        0 -> CommandTokensHelpContent()
                        1 -> SpeedballHelpContent()
                        2 -> BootyHelpContent()
                        3 -> MetachemistryHelpContent()
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Close Button
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(R.string.help_close))
                }
            }
        }
    }
}

@Composable
private fun CommandTokensHelpContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.help_command_tokens_title),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        // Strategic Use
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
                    text = stringResource(R.string.help_strategic_title),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.help_strategic_limit),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                HelpSectionLabel(stringResource(R.string.help_procedural_label))

                HelpSubHeader(icon = "1️⃣", title = stringResource(R.string.help_if_first))
                HelpItemRow(
                    icon = "🥇",
                    title = stringResource(R.string.help_if_first_title),
                    desc = stringResource(R.string.help_if_first_desc)
                )

                HelpSubHeader(icon = "2️⃣", title = stringResource(R.string.help_if_second))
                HelpItemRow(
                    icon = "🚫",
                    title = stringResource(R.string.help_second_remove_title),
                    desc = stringResource(R.string.help_second_remove_desc)
                )
                HelpItemRow(
                    icon = "🔒",
                    title = stringResource(R.string.help_second_limit_title),
                    desc = stringResource(R.string.help_second_limit_desc)
                )
                HelpItemRow(
                    icon = "🔥",
                    title = stringResource(R.string.help_second_suppressive_title),
                    desc = stringResource(R.string.help_second_suppressive_desc)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 2.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )

                HelpItemRow(
                    icon = "📦",
                    title = stringResource(R.string.help_logistical_title),
                    desc = stringResource(R.string.help_logistical_desc)
                )
            }
        }

        // Operational Use
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
                    text = stringResource(R.string.help_operational_title),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.help_operational_note),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                HelpItemRow(
                    icon = "👥",
                    title = stringResource(R.string.help_coordinated_title),
                    desc = stringResource(R.string.help_coordinated_desc)
                )
                HelpItemRow(
                    icon = "🎲",
                    title = stringResource(R.string.help_guts_title),
                    desc = stringResource(R.string.help_guts_desc)
                )
                HelpItemRow(
                    icon = "🔄",
                    title = stringResource(R.string.help_convert_title),
                    desc = stringResource(R.string.help_convert_desc)
                )
                HelpItemRow(
                    icon = "🛡️",
                    title = stringResource(R.string.help_retreat_title),
                    desc = stringResource(R.string.help_retreat_desc)
                )
                HelpItemRow(
                    icon = "💉",
                    title = stringResource(R.string.help_doctor_title),
                    desc = stringResource(R.string.help_doctor_desc)
                )
                HelpItemRow(
                    icon = "🔧",
                    title = stringResource(R.string.help_engineer_title),
                    desc = stringResource(R.string.help_engineer_desc)
                )
                HelpItemRow(
                    icon = "⚔️",
                    title = stringResource(R.string.help_fireteam_title),
                    desc = stringResource(R.string.help_fireteam_desc)
                )
            }
        }

        // Executive Use
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
                    text = stringResource(R.string.help_executive_title),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.help_executive_note),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                HelpItemRow(
                    icon = "🔀",
                    title = stringResource(R.string.help_regroup_title),
                    desc = stringResource(R.string.help_regroup_desc)
                )
                HelpItemRow(
                    icon = "🧿",
                    title = stringResource(R.string.help_possessed_title),
                    desc = stringResource(R.string.help_possessed_desc)
                )
            }
        }
    }
}

@Composable
private fun HelpSectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 9.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun HelpSubHeader(
    icon: String,
    title: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = icon, fontSize = 11.sp)
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun SpeedballHelpContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.help_speedball_title),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Text(
            text = stringResource(R.string.help_speedball_note),
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

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
                HelpItemRow(
                    icon = "1️⃣",
                    title = stringResource(R.string.help_first_token_title),
                    desc = stringResource(R.string.help_first_token_desc)
                )
                HelpItemRow(
                    icon = "2️⃣",
                    title = stringResource(R.string.help_second_token_title),
                    desc = stringResource(R.string.help_second_token_desc)
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DiceTableRow(
                    diceRange = "1–3",
                    icon = "💉",
                    title = stringResource(R.string.help_vitapack_title),
                    desc = stringResource(R.string.help_vitapack_desc)
                )
                DiceTableRow(
                    diceRange = "4–6",
                    icon = "🔧",
                    title = stringResource(R.string.help_autorepair_title),
                    desc = stringResource(R.string.help_autorepair_desc)
                )
                DiceTableRow(
                    diceRange = "7–10",
                    icon = "⚡",
                    title = stringResource(R.string.help_switch_on_title),
                    desc = stringResource(R.string.help_switch_on_desc)
                )
                DiceTableRow(
                    diceRange = "11–13",
                    icon = "🚀",
                    title = stringResource(R.string.help_jetpack_title),
                    desc = stringResource(R.string.help_jetpack_desc)
                )
                DiceTableRow(
                    diceRange = "14–16",
                    icon = "💥",
                    title = stringResource(R.string.help_overkill_title),
                    desc = stringResource(R.string.help_overkill_desc)
                )
                DiceTableRow(
                    diceRange = "17–20",
                    icon = "🛡️",
                    title = stringResource(R.string.help_nanoshield_title),
                    desc = stringResource(R.string.help_nanoshield_desc)
                )
            }
        }

        Text(
            text = stringResource(R.string.help_speedball_restrictions),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun BootyHelpContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.help_booty_title),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Text(
            text = stringResource(R.string.help_booty_note),
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DiceTableRow(
                    diceRange = "1–2",
                    icon = "🛡️",
                    title = stringResource(R.string.help_booty_arm1_title),
                    desc = stringResource(R.string.help_booty_arm1_desc)
                )
                DiceTableRow(
                    diceRange = "3–4",
                    icon = "🔥",
                    title = stringResource(R.string.help_booty_flamethrower_title),
                    desc = stringResource(R.string.help_booty_flamethrower_desc)
                )
                DiceTableRow(
                    diceRange = "5–6",
                    icon = "💣",
                    title = stringResource(R.string.help_booty_grenades_title),
                    desc = stringResource(R.string.help_booty_grenades_desc)
                )
                DiceTableRow(
                    diceRange = "7–8",
                    icon = "🗡️",
                    title = stringResource(R.string.help_booty_da_cc_title),
                    desc = stringResource(R.string.help_booty_da_cc_desc)
                )
                DiceTableRow(
                    diceRange = "9",
                    icon = "👁️",
                    title = stringResource(R.string.help_booty_visor_title),
                    desc = stringResource(R.string.help_booty_visor_desc)
                )
                DiceTableRow(
                    diceRange = "10",
                    icon = "⚔️",
                    title = stringResource(R.string.help_booty_exp_cc_title),
                    desc = stringResource(R.string.help_booty_exp_cc_desc)
                )
                DiceTableRow(
                    diceRange = "11",
                    icon = "🕸️",
                    title = stringResource(R.string.help_booty_adhesive_title),
                    desc = stringResource(R.string.help_booty_adhesive_desc)
                )
                DiceTableRow(
                    diceRange = "12",
                    icon = "🛡️",
                    title = stringResource(R.string.help_booty_immunity_ap_title),
                    desc = stringResource(R.string.help_booty_immunity_ap_desc)
                )
                DiceTableRow(
                    diceRange = "13",
                    icon = "🚀",
                    title = stringResource(R.string.help_booty_panzerfaust_title),
                    desc = stringResource(R.string.help_booty_panzerfaust_desc)
                )
                DiceTableRow(
                    diceRange = "14",
                    icon = "🗡️",
                    title = stringResource(R.string.help_booty_monofilament_title),
                    desc = stringResource(R.string.help_booty_monofilament_desc)
                )
                DiceTableRow(
                    diceRange = "15",
                    icon = "🏃",
                    title = stringResource(R.string.help_booty_mov84_title),
                    desc = stringResource(R.string.help_booty_mov84_desc)
                )
                DiceTableRow(
                    diceRange = "16",
                    icon = "🔫",
                    title = stringResource(R.string.help_booty_shock_title),
                    desc = stringResource(R.string.help_booty_shock_desc)
                )
                DiceTableRow(
                    diceRange = "17",
                    icon = "🎯",
                    title = stringResource(R.string.help_booty_sniper_title),
                    desc = stringResource(R.string.help_booty_sniper_desc)
                )
                DiceTableRow(
                    diceRange = "18",
                    icon = "🛡️",
                    title = stringResource(R.string.help_booty_immunity_arm_title),
                    desc = stringResource(R.string.help_booty_immunity_arm_desc)
                )
                DiceTableRow(
                    diceRange = "19",
                    icon = "👻",
                    title = stringResource(R.string.help_booty_mimetism_title),
                    desc = stringResource(R.string.help_booty_mimetism_desc)
                )
                DiceTableRow(
                    diceRange = "20",
                    icon = "💥",
                    title = stringResource(R.string.help_booty_hmg_title),
                    desc = stringResource(R.string.help_booty_hmg_desc)
                )
            }
        }

        Text(
            text = stringResource(R.string.help_booty_tag_note),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = stringResource(R.string.help_free_roll_note),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MetachemistryHelpContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.help_metachemistry_title),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Text(
            text = stringResource(R.string.help_metachemistry_note),
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DiceTableRow(
                    diceRange = "1–3",
                    icon = "💪",
                    title = stringResource(R.string.help_meta_ph3_title),
                    desc = stringResource(R.string.help_meta_ph3_desc)
                )
                DiceTableRow(
                    diceRange = "4–5",
                    icon = "🦘",
                    title = stringResource(R.string.help_meta_superjump_title),
                    desc = stringResource(R.string.help_meta_superjump_desc)
                )
                DiceTableRow(
                    diceRange = "6–7",
                    icon = "♻️",
                    title = stringResource(R.string.help_meta_regen_title),
                    desc = stringResource(R.string.help_meta_regen_desc)
                )
                DiceTableRow(
                    diceRange = "8–10",
                    icon = "🧗",
                    title = stringResource(R.string.help_meta_climbing_title),
                    desc = stringResource(R.string.help_meta_climbing_desc)
                )
                DiceTableRow(
                    diceRange = "11–12",
                    icon = "🏃",
                    title = stringResource(R.string.help_meta_mov84_title),
                    desc = stringResource(R.string.help_meta_mov84_desc)
                )
                DiceTableRow(
                    diceRange = "13–14",
                    icon = "🛡️",
                    title = stringResource(R.string.help_meta_bts6_title),
                    desc = stringResource(R.string.help_meta_bts6_desc)
                )
                DiceTableRow(
                    diceRange = "15",
                    icon = "❤️",
                    title = stringResource(R.string.help_meta_nwi_title),
                    desc = stringResource(R.string.help_meta_nwi_desc)
                )
                DiceTableRow(
                    diceRange = "16",
                    icon = "🛡️",
                    title = stringResource(R.string.help_meta_arm_immunity_title),
                    desc = stringResource(R.string.help_meta_arm_immunity_desc)
                )
                DiceTableRow(
                    diceRange = "17",
                    icon = "🦁",
                    title = stringResource(R.string.help_meta_dogged_title),
                    desc = stringResource(R.string.help_meta_dogged_desc)
                )
                DiceTableRow(
                    diceRange = "18",
                    icon = "🦘",
                    title = stringResource(R.string.help_meta_mov64_superjump_title),
                    desc = stringResource(R.string.help_meta_mov64_superjump_desc)
                )
                DiceTableRow(
                    diceRange = "19",
                    icon = "🧗",
                    title = stringResource(R.string.help_meta_mov84_climbing_title),
                    desc = stringResource(R.string.help_meta_mov84_climbing_desc)
                )
                DiceTableRow(
                    diceRange = "20",
                    icon = "🧪",
                    title = stringResource(R.string.help_meta_ph3_regen_title),
                    desc = stringResource(R.string.help_meta_ph3_regen_desc)
                )
            }
        }

        Text(
            text = stringResource(R.string.help_meta_note),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = stringResource(R.string.help_free_roll_note),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun HelpItemRow(
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
                .size(30.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(desc, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun DiceTableRow(
    diceRange: String,
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
                .size(32.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(4.dp))
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.help_d20_format, diceRange),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(desc, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
