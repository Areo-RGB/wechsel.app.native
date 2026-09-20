package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Player
import com.example.model.PlayerStatus
import com.example.ui.WexelViewModel
import com.example.ui.components.FullscreenButton
import com.example.ui.components.PlayerAvatarBadge
import com.example.ui.theme.WexelBlue
import com.example.ui.theme.WexelBlueDark
import com.example.ui.theme.WexelBorder
import com.example.ui.theme.WexelDark
import com.example.ui.theme.WexelError
import com.example.ui.theme.WexelGray
import com.example.ui.theme.WexelLightGray
import com.example.ui.theme.WexelSurfaceLight

@Composable
fun KaderScreen(
    players: List<Player>,
    isFullscreen: Boolean,
    onToggleFullscreen: () -> Unit,
    onCycleStatus: (String) -> Unit,
    onAddPlayer: (String) -> Unit,
    onDeletePlayer: (String) -> Unit,
    onNavigateToAufstellung: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newPlayerName by remember { mutableStateOf("") }
    var playerToDelete by remember { mutableStateOf<Player?>(null) }

    val fieldPlayersCount = players.count { it.status == PlayerStatus.FIELD }
    val benchPlayersCount = players.count { it.status == PlayerStatus.BENCH }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Header with Tabs & Fullscreen
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(WexelSurfaceLight)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Active "Kader" Tab
                Box(
                    modifier = Modifier
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Kader",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = WexelDark
                        )
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(WexelError),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "!",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(WexelBlue)
                    )
                }

                // Inactive "Nummern" Tab
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Nummern",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = WexelGray
                    )
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(WexelError),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "!",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            FullscreenButton(
                isFullscreen = isFullscreen,
                onToggle = onToggleFullscreen
            )
        }
        HorizontalDivider(thickness = 1.dp, color = WexelBorder)

        // Stats Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
                .background(Color.White)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Checkroom,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = WexelBlue
                )
                Text(
                    text = "Aufgestellt:",
                    fontSize = 11.sp,
                    color = WexelGray
                )
                Text(
                    text = "$fieldPlayersCount/${WexelViewModel.MAX_FIELD_COUNT}",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = WexelDark
                )
                Text(text = "|", color = WexelBorder)
                Text(
                    text = "Nicht aufgestellt:",
                    fontSize = 11.sp,
                    color = WexelGray
                )
                Text(
                    text = "$benchPlayersCount",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = WexelDark
                )
            }

            Row(
                modifier = Modifier
                    .testTag("add_player_button")
                    .clickable { showAddDialog = true }
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Spieler hinzufügen",
                    modifier = Modifier.size(13.dp),
                    tint = WexelBlue
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "+ Spieler",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WexelBlue
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = WexelBorder)

        // Players List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(players, key = { it.id }) { player ->
                val isField = player.status == PlayerStatus.FIELD

                Row(
                    modifier = Modifier
                        .testTag("player_row_${player.id}")
                        .fillMaxWidth()
                        .height(46.dp)
                        .clickable { onCycleStatus(player.id) }
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Avatar & Name
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PlayerAvatarBadge(
                            name = player.name,
                            isActive = isField
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = player.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isField) WexelDark else WexelGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Right: Status Toggle Button & Delete Icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .testTag("status_toggle_${player.id}")
                                .width(136.dp)
                                .height(28.dp)
                                .background(if (isField) WexelBlue else WexelSurfaceLight)
                                .border(1.dp, if (isField) WexelBlue else WexelBorder)
                                .clickable { onCycleStatus(player.id) },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (isField) {
                                    Icon(
                                        imageVector = Icons.Default.Checkroom,
                                        contentDescription = null,
                                        modifier = Modifier.size(13.dp),
                                        tint = Color.White
                                    )
                                    Text(
                                        text = "AUFGESTELLT",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        letterSpacing = 0.5.sp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Block,
                                        contentDescription = null,
                                        modifier = Modifier.size(11.dp),
                                        tint = WexelLightGray
                                    )
                                    Text(
                                        text = "NICHT AUFGESTELLT",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = WexelGray,
                                        letterSpacing = 0.3.sp
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = { playerToDelete = player },
                            modifier = Modifier
                                .testTag("delete_player_${player.id}")
                                .size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Löschen",
                                modifier = Modifier.size(15.dp),
                                tint = WexelLightGray
                            )
                        }
                    }
                }
                HorizontalDivider(thickness = 0.5.dp, color = WexelBorder)
            }
        }

        // Bottom Sticky Action Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(WexelSurfaceLight)
        ) {
            HorizontalDivider(thickness = 1.dp, color = WexelBorder)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$fieldPlayersCount Spieler aufgestellt",
                    fontSize = 12.sp,
                    color = WexelGray
                )

                Button(
                    onClick = onNavigateToAufstellung,
                    modifier = Modifier
                        .testTag("go_to_aufstellung_button")
                        .height(30.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WexelBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Zur Aufstellung",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }

    // Add Player Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    text = "Neuen Spieler anlegen",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WexelDark
                )
            },
            text = {
                Column {
                    Text(
                        text = "Name des Spielers",
                        fontSize = 11.sp,
                        color = WexelGray,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = newPlayerName,
                        onValueChange = { newPlayerName = it },
                        placeholder = { Text("z.B. Lukas oder Max Schmidt", fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier
                            .testTag("add_player_input")
                            .fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPlayerName.isNotBlank()) {
                            onAddPlayer(newPlayerName)
                            newPlayerName = ""
                            showAddDialog = false
                        }
                    },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = WexelBlue)
                ) {
                    Text("Speichern", fontSize = 12.sp)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showAddDialog = false },
                    shape = RectangleShape
                ) {
                    Text("Abbrechen", fontSize = 12.sp, color = WexelDark)
                }
            },
            shape = RectangleShape,
            containerColor = Color.White
        )
    }

    // Delete Player Dialog
    playerToDelete?.let { player ->
        AlertDialog(
            onDismissRequest = { playerToDelete = null },
            title = {
                Text(
                    text = "Spieler entfernen?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WexelError
                )
            },
            text = {
                Text(
                    text = "Möchtest du \"${player.name}\" wirklich aus dem Kader entfernen?",
                    fontSize = 12.sp,
                    color = WexelDark
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeletePlayer(player.id)
                        playerToDelete = null
                    },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = WexelError)
                ) {
                    Text("Entfernen", fontSize = 12.sp)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { playerToDelete = null },
                    shape = RectangleShape
                ) {
                    Text("Abbrechen", fontSize = 12.sp, color = WexelDark)
                }
            },
            shape = RectangleShape,
            containerColor = Color.White
        )
    }
}
