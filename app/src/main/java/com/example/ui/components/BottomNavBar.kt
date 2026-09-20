package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TabId
import com.example.ui.theme.WexelBlue
import com.example.ui.theme.WexelBorder
import com.example.ui.theme.WexelGray

data class NavTabItem(
    val id: TabId,
    val label: String,
    val icon: ImageVector
)

@Composable
fun BottomNavBar(
    activeTab: TabId,
    onTabSelected: (TabId) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavTabItem(TabId.KADER, "Kader", Icons.Default.People),
        NavTabItem(TabId.AUFSTELLUNG, "Aufstellung", Icons.Default.Dashboard),
        NavTabItem(TabId.DATEN, "Daten", Icons.Default.BarChart)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        HorizontalDivider(thickness = 1.dp, color = WexelBorder)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            items.forEach { tabItem ->
                val isActive = activeTab == tabItem.id
                Box(
                    modifier = Modifier
                        .testTag("nav_tab_${tabItem.id.name.lowercase()}")
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { onTabSelected(tabItem.id) },
                    contentAlignment = Alignment.Center
                ) {
                    if (isActive) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(WexelBlue)
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = tabItem.icon,
                            contentDescription = tabItem.label,
                            modifier = Modifier.size(20.dp),
                            tint = if (isActive) WexelBlue else WexelGray
                        )
                        Text(
                            text = tabItem.label,
                            fontSize = 11.sp,
                            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isActive) WexelBlue else WexelGray,
                            letterSpacing = 0.2.sp
                        )
                    }
                }
            }
        }
    }
}
