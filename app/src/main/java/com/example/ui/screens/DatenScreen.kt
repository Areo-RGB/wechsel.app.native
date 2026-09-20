package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Player
import com.example.model.TimeUtils
import com.example.ui.components.FullscreenButton
import com.example.ui.components.PlayerAvatarBadge
import com.example.ui.theme.WexelBlue
import com.example.ui.theme.WexelBorder
import com.example.ui.theme.WexelDark
import com.example.ui.theme.WexelGray
import com.example.ui.theme.WexelLightGray
import com.example.ui.theme.WexelSurfaceLight

@Composable
fun DatenScreen(
    players: List<Player>,
    matchElapsed: Long,
    isFullscreen: Boolean,
    onToggleFullscreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Sort players by percentage descending, then by feldzeit descending
    val sortedPlayers = players
        .sortedWith(
            compareByDescending<Player> { it.getPercentage(matchElapsed) }
                .thenByDescending { it.feldzeit }
                .thenBy { it.name }
        )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(WexelSurfaceLight)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "SPIELDATEN & EINSATZZEITEN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = WexelGray,
                letterSpacing = 0.5.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${sortedPlayers.size} Spieler",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = WexelDark
                )
                FullscreenButton(
                    isFullscreen = isFullscreen,
                    onToggle = onToggleFullscreen
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = WexelBorder)

        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(WexelSurfaceLight)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "NAME",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = WexelGray,
                modifier = Modifier.weight(2f),
                letterSpacing = 0.3.sp
            )
            Text(
                text = "SPIELZEIT",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = WexelGray,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1.3f),
                letterSpacing = 0.3.sp
            )
            Text(
                text = "ANTEIL",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = WexelGray,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1.1f),
                letterSpacing = 0.3.sp
            )
            Text(
                text = "GESAMT",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = WexelGray,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1.3f),
                letterSpacing = 0.3.sp
            )
        }
        HorizontalDivider(thickness = 1.dp, color = WexelBorder)

        // Table Rows
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(sortedPlayers, key = { it.id }) { player ->
                val pct = player.getPercentage(matchElapsed)

                Row(
                    modifier = Modifier
                        .testTag("daten_row_${player.id}")
                        .fillMaxWidth()
                        .height(44.dp)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Name with avatar
                    Row(
                        modifier = Modifier.weight(2f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PlayerAvatarBadge(name = player.name, size = 22.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = player.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = WexelDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Field Playtime
                    Text(
                        text = TimeUtils.formatTime(player.feldzeit),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Medium,
                        color = WexelDark,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1.3f)
                    )

                    // Percentage
                    Text(
                        text = "$pct%",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = WexelBlue,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1.1f)
                    )

                    // Total
                    Text(
                        text = TimeUtils.formatTime(player.totalTime),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = WexelLightGray,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1.3f)
                    )
                }
                HorizontalDivider(thickness = 0.5.dp, color = WexelBorder)
            }

            if (sortedPlayers.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Noch keine Spieldaten vorhanden",
                            fontSize = 12.sp,
                            color = WexelLightGray
                        )
                    }
                }
            }
        }
    }
}
