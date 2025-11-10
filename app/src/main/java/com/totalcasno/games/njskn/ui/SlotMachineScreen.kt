package com.totalcasno.games.njskn.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.totalcasno.games.njskn.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SlotMachineScreen(
    slotCount: Int = 3,
    symbols: List<String> = listOf("🎰", "💎", "🍀", "⭐", "🔔", "🎲", "💰", "🎁"),
    onStartRequested: () -> Unit,
    onSettingsClick: () -> Unit,
    isLoading: Boolean = false
) {
    var isRolling by remember { mutableStateOf(false) }
    var slotValues by remember(slotCount, symbols) { mutableStateOf(symbols.take(slotCount)) }
    var matchResult by remember { mutableStateOf<MatchResult?>(null) }
    
    LaunchedEffect(slotCount, symbols) {
        slotValues = symbols.take(slotCount)
        matchResult = null
    }
    
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val availableWidth = screenWidth - 80.dp
    val slotSize = (availableWidth / slotCount) - 8.dp
    
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
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(48.dp)
        ) {
            Text(
                text = "⚙️",
                fontSize = 32.sp
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "LUCKY REELS",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = GlowBlue,
                modifier = Modifier.padding(bottom = 48.dp)
            )
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                SlotBackground,
                                MediumBlue.copy(alpha = 0.3f)
                            )
                        )
                    )
                    .border(
                        width = 3.dp,
                        color = SlotBorder,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    slotValues.forEach { symbol ->
                        SlotReel(
                            symbol = symbol, 
                            isRolling = isRolling,
                            size = slotSize
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(64.dp))
            
            if (isLoading) {
                CircularProgressIndicator(
                    color = AccentBlue,
                    modifier = Modifier.size(56.dp)
                )
            } else {
                Button(
                    onClick = {
                        if (!isRolling) {
                            isRolling = true
                            matchResult = null
                            coroutineScope.launch {
                                delay(100)
                                repeat(15) {
                                    slotValues = List(slotCount) { symbols.random() }
                                    delay(100)
                                }
                                delay(500)
                                isRolling = false
                                matchResult = checkMatches(slotValues, slotCount)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(70.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentBlue,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(35.dp),
                    enabled = !isRolling
                ) {
                    Text(
                        text = if (isRolling) "ROLLING..." else "PRESS TO PLAY",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            AnimatedVisibility(
                visible = matchResult != null,
                enter = scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                matchResult?.let { result ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        result.color,
                                        result.color.copy(alpha = 0.7f)
                                    )
                                )
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = result.emoji,
                                fontSize = 48.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = result.message,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            
            AnimatedVisibility(
                visible = matchResult == null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "Press the button to start your luck!",
                    fontSize = 16.sp,
                    color = BrightBlue.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

data class MatchResult(
    val matchCount: Int,
    val message: String,
    val emoji: String,
    val color: Color
)

fun checkMatches(slotValues: List<String>, slotCount: Int): MatchResult? {
    val grouped = slotValues.groupBy { it }
    val maxMatch = grouped.maxByOrNull { it.value.size }?.value?.size ?: 0
    
    return when {
        maxMatch == slotCount -> {
            MatchResult(
                matchCount = maxMatch,
                message = "JACKPOT! ALL MATCH!",
                emoji = "🎉",
                color = Color(0xFFFFD700)
            )
        }
        maxMatch == slotCount - 1 && slotCount >= 4 -> {
            MatchResult(
                matchCount = maxMatch,
                message = "AMAZING! $maxMatch MATCH!",
                emoji = "🔥",
                color = Color(0xFFFF6B35)
            )
        }
        maxMatch == slotCount - 1 && slotCount == 3 -> {
            MatchResult(
                matchCount = maxMatch,
                message = "NICE! 2 MATCH!",
                emoji = "⭐",
                color = Color(0xFF4A90E2)
            )
        }
        maxMatch == 2 && slotCount == 3 -> {
            MatchResult(
                matchCount = maxMatch,
                message = "NICE! 2 MATCH!",
                emoji = "⭐",
                color = Color(0xFF4A90E2)
            )
        }
        else -> null
    }
}

@Composable
fun SlotReel(
    symbol: String,
    isRolling: Boolean,
    size: androidx.compose.ui.unit.Dp = 100.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "slot_roll")
    
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isRolling) 100f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset_y"
    )
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isRolling) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    val fontSize = (size.value * 0.45f).sp
    
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape((size.value * 0.16f).dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        LightBlue,
                        DarkBlue
                    )
                )
            )
            .border(
                width = 2.dp,
                color = AccentBlue,
                shape = RoundedCornerShape((size.value * 0.16f).dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            fontSize = fontSize,
            textAlign = TextAlign.Center
        )
    }
}
