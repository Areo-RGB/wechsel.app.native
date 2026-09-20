package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WexelBorder
import com.example.ui.theme.WexelDark
import com.example.ui.theme.WexelSurfaceLight

@Composable
fun PlayerAvatarBadge(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 26.dp,
    isActive: Boolean = true
) {
    val initials = name.trim().take(2).uppercase()

    Box(
        modifier = modifier
            .size(size)
            .background(WexelSurfaceLight)
            .border(1.dp, WexelBorder),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            fontSize = (size.value * 0.42).sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isActive) WexelDark else Color.Gray
        )
    }
}
