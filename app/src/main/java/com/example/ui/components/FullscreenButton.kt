package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WexelBlue
import com.example.ui.theme.WexelBorder
import com.example.ui.theme.WexelGray

@Composable
fun FullscreenButton(
    isFullscreen: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    showLabel: Boolean = false
) {
    Box(
        modifier = modifier
            .testTag("fullscreen_toggle_button")
            .height(30.dp)
            .border(1.dp, WexelBorder)
            .background(Color.White)
            .clickable(onClick = onToggle)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                contentDescription = if (isFullscreen) "Vollbild beenden" else "Vollbild aktivieren",
                modifier = Modifier.size(16.dp),
                tint = if (isFullscreen) WexelBlue else WexelGray
            )
            if (showLabel) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isFullscreen) "Beenden" else "Vollbild",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isFullscreen) WexelBlue else WexelGray
                )
            }
        }
    }
}
