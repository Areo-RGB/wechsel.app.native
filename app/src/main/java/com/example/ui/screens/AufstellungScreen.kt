package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MatchState
import com.example.model.MatchStatus
import com.example.model.Player
import com.example.model.PlayerStatus
import com.example.model.TimeDisplayMode
import com.example.model.TimeUtils
import com.example.ui.WexelViewModel
import com.example.ui.components.FullscreenButton
import com.example.ui.components.PlayerAvatarBadge
import com.example.ui.theme.WexelBlue
import com.example.ui.theme.WexelBorder
import com.example.ui.theme.WexelBorderDark
import com.example.ui.theme.WexelDark
import com.example.ui.theme.WexelGray
import com.example.ui.theme.WexelLightGray
import com.example.ui.theme.WexelSuccess
import com.example.ui.theme.WexelSurfaceLight
import com.example.ui.theme.WexelWarning

@Composable
fun AufstellungScreen(
    players: List<Player>,
    matchState: MatchState,
    timeDisplayMode: TimeDisplayMode,
    isFullscreen: Boolean,
    onToggleMatch: () -> Unit,
    onResetMatch: () -> Unit,
    onToggleFullscreen: () -> Unit,
    onCycleTimeMode: () -> Unit,
    onBringToField: (String) -> Unit,
    onMoveToBench: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val fieldPlayers = players.filter { it.status == PlayerStatus.FIELD }
    val benchPlayers = players.filter { it.status == PlayerStatus.BENCH }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Match Control Ribbon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(Color.White)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Timer & Status Indicator & Play/Pause
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Status dot
                val dotColor = when (matchState.status) {
                    MatchStatus.RUNNING -> WexelSuccess
                    MatchStatus.PAUSED -> WexelWarning
                    MatchStatus.IDLE -> WexelLightGray
                }
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .alpha(if (matchState.status == MatchStatus.RUNNING) pulseAlpha else 1f)
                        .background(dotColor)
                )

                // Large Monospace Timer
                Text(
                    text = TimeUtils.formatTime(matchState.elapsed),
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    color = WexelDark,
                    letterSpacing = (-0.5).sp
                )

                // Play / Pause Button
                Button(
                    onClick = onToggleMatch,
                    modifier = Modifier
                        .testTag("match_play_pause_button")
                        .height(28.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (matchState.status == MatchStatus.RUNNING) WexelDark else WexelBlue,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = if (matchState.status == MatchStatus.RUNNING) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (matchState.status == MatchStatus.RUNNING) "Pause" else "Start",
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (matchState.status == MatchStatus.RUNNING) "Pause" else "Start",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Right Controls: Reset & Fullscreen
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (matchState.elapsed > 0) {
                    Box(
                        modifier = Modifier
                            .testTag("match_reset_button")
                            .height(30.dp)
                            .border(1.dp, WexelBorder)
                            .background(WexelSurfaceLight)
                            .clickable(onClick = onResetMatch)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset",
                                modifier = Modifier.size(13.dp),
                                tint = WexelGray
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Reset",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = WexelGray
                            )
                        }
                    }
                }

                FullscreenButton(
                    isFullscreen = isFullscreen,
                    onToggle = onToggleFullscreen
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = WexelBorder)

        // Main List Content
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // Aufstellung Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(WexelSurfaceLight)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Checkroom,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = WexelBlue
                        )
                        Text(
                            text = "AUFSTELLUNG",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = WexelGray,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "(${fieldPlayers.size}/${WexelViewModel.MAX_FIELD_COUNT})",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = WexelDark
                        )
                    }
                    Text(
                        text = "Auf dem Platz",
                        fontSize = 10.sp,
                        color = WexelLightGray
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = WexelBorder)
            }

            // Field Players Rows
            items(fieldPlayers, key = { "field_${it.id}" }) { player ->
                Row(
                    modifier = Modifier
                        .testTag("field_player_row_${player.id}")
                        .fillMaxWidth()
                        .height(46.dp)
                        .clickable(onClick = onCycleTimeMode)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Avatar & Name
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PlayerAvatarBadge(name = player.name, isActive = true)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = player.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = WexelDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Right: Playtime / Percent & "+ Bank" Button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Playtime indicator
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (timeDisplayMode != TimeDisplayMode.PERCENT) {
                                Text(
                                    text = TimeUtils.formatTime(player.feldzeit),
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Medium,
                                    color = WexelDark
                                )
                            }
                            if (timeDisplayMode != TimeDisplayMode.TIME) {
                                Text(
                                    text = "${player.getPercentage(matchState.elapsed)}%",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = WexelBlue
                                )
                            }
                        }

                        // "+ Bank" Button
                        Box(
                            modifier = Modifier
                                .testTag("move_to_bench_${player.id}")
                                .height(26.dp)
                                .border(1.dp, WexelBorderDark)
                                .background(Color.White)
                                .clickable { onMoveToBench(player.id) }
                                .padding(horizontal = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+ Bank",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = WexelGray
                            )
                        }
                    }
                }
                HorizontalDivider(thickness = 0.5.dp, color = WexelBorder)
            }

            if (fieldPlayers.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Keine Spieler auf dem Platz. Klicke bei einem Bankspieler auf \"+ Feld\".",
                            fontSize = 11.sp,
                            color = WexelLightGray
                        )
                    }
                }
            }

            // Ersatzbank Section Header
            item {
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(thickness = 1.dp, color = WexelBorder)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(WexelSurfaceLight)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Weekend,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = WexelGray
                        )
                        Text(
                            text = "ERSATZBANK",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = WexelGray,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "(${benchPlayers.size})",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = WexelDark
                        )
                    }
                    Text(
                        text = "Verfügbar",
                        fontSize = 10.sp,
                        color = WexelLightGray
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = WexelBorder)
            }

            // Bench Players Rows
            items(benchPlayers, key = { "bench_${it.id}" }) { player ->
                Row(
                    modifier = Modifier
                        .testTag("bench_player_row_${player.id}")
                        .fillMaxWidth()
                        .height(44.dp)
                        .clickable { onBringToField(player.id) }
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Avatar & Name
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PlayerAvatarBadge(name = player.name, isActive = false)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = player.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = WexelDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Right: Playtime / Percent & "+ Feld" Button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (timeDisplayMode != TimeDisplayMode.PERCENT) {
                                Text(
                                    text = TimeUtils.formatTime(player.feldzeit),
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = WexelLightGray
                                )
                            }
                            if (timeDisplayMode != TimeDisplayMode.TIME) {
                                Text(
                                    text = "${player.getPercentage(matchState.elapsed)}%",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.SemiBold,
                                    color = WexelGray
                                )
                            }
                        }

                        // "+ Feld" Button
                        Box(
                            modifier = Modifier
                                .testTag("bring_to_field_${player.id}")
                                .height(26.dp)
                                .border(1.dp, WexelBlue.copy(alpha = 0.5f))
                                .background(WexelBlue.copy(alpha = 0.05f))
                                .clickable { onBringToField(player.id) }
                                .padding(horizontal = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+ Feld",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = WexelBlue
                            )
                        }
                    }
                }
                HorizontalDivider(thickness = 0.5.dp, color = WexelBorder)
            }

            if (benchPlayers.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Keine Spieler auf der Bank",
                            fontSize = 11.sp,
                            color = WexelLightGray
                        )
                    }
                }
            }
        }

        // Bottom Summary Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(WexelSurfaceLight)
                .border(1.dp, WexelBorder)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Aufgestellt: ", fontSize = 11.sp, color = WexelGray)
                Text(
                    text = "${fieldPlayers.size}/${WexelViewModel.MAX_FIELD_COUNT}",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = WexelDark
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Bank: ", fontSize = 11.sp, color = WexelGray)
                Text(
                    text = "${benchPlayers.size}",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = WexelDark
                )
            }
        }
    }
}
