package mohit.dev.passwordmanager.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import mohit.dev.passwordmanager.viewmodel.LockViewModel

@Composable
fun PinLockScreen(
    viewModel: LockViewModel = hiltViewModel(),
    onUnlock: () -> Unit
) {
    val pin by viewModel.pin.collectAsState()
    val error by viewModel.error.collectAsState()

    // Animated error color
    val textColor by animateColorAsState(
        targetValue = if (error) Color.Red else Color.White,
        animationSpec = tween(300),
        label = "textColor"
    )

    LaunchedEffect(pin) {
        if (pin.length == 4) {
            if (viewModel.validatePin(pin)) onUnlock()
            else viewModel.clearPin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 24.dp, vertical = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        // Title
        Text(
            text = if (error) "Wrong PIN" else "Enter PIN",
            color = textColor,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(40.dp))

        // PIN Dots — clean & modern
        Row(horizontalArrangement = Arrangement.Center) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(
                            if (index < pin.length) Color.White
                            else Color(0xFF505050)
                        )
                )
            }
        }

        Spacer(Modifier.height(60.dp))

        // Keypad Layout
        val keypadRows = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("", "0", "⌫")
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            keypadRows.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    row.forEach { key ->
                        KeyButton(
                            label = key,
                            onClick = {
                                when (key) {
                                    "⌫" -> viewModel.removeDigit()
                                    "" -> {}
                                    else -> viewModel.addDigit(key)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KeyButton(label: String, onClick: () -> Unit) {
    if (label.isEmpty()) {
        Spacer(modifier = Modifier.size(80.dp))
        return
    }

    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color(0xFF1A1A1A))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
