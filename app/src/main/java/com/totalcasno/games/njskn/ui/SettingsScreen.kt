package com.totalcasno.games.njskn.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.totalcasno.games.njskn.ui.theme.*
import com.totalcasno.games.njskn.utils.SlotTheme

@Composable
fun SettingsScreen(
    currentSlotCount: Int,
    currentTheme: String,
    onSlotCountChanged: (Int) -> Unit,
    onThemeChanged: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DarkBlue,
                        MediumBlue,
                        DarkBlue
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "SETTINGS",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = GlowBlue,
                modifier = Modifier.padding(bottom = 48.dp)
            )
            
            Text(
                text = "Number of Reels",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = BrightBlue,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(3, 4, 5).forEach { count ->
                    SlotCountOption(
                        count = count,
                        isSelected = currentSlotCount == count,
                        onClick = { onSlotCountChanged(count) }
                    )
                }
            }
            
            Text(
                text = "Theme",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = BrightBlue,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SlotTheme.values().forEach { theme ->
                    ThemeOption(
                        theme = theme,
                        isSelected = currentTheme.equals(theme.name, ignoreCase = true),
                        onClick = { onThemeChanged(theme.name) }
                    )
                }
            }
            
            Button(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(
                    text = "BACK TO GAME",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SlotCountOption(
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) {
                    Brush.radialGradient(
                        colors = listOf(AccentBlue, LightBlue)
                    )
                } else {
                    Brush.radialGradient(
                        colors = listOf(LightBlue.copy(alpha = 0.3f), DarkBlue)
                    )
                }
            )
            .border(
                width = if (isSelected) 3.dp else 2.dp,
                color = if (isSelected) GlowBlue else AccentBlue,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = count.toString(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun ThemeOption(
    theme: SlotTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) {
                    Brush.horizontalGradient(
                        colors = listOf(AccentBlue, LightBlue)
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(LightBlue.copy(alpha = 0.3f), DarkBlue)
                    )
                }
            )
            .border(
                width = if (isSelected) 3.dp else 2.dp,
                color = if (isSelected) GlowBlue else AccentBlue,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = theme.displayName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            theme.symbols.take(4).forEach { symbol ->
                Text(
                    text = symbol,
                    fontSize = 24.sp
                )
            }
        }
    }
}

