package mohit.dev.passwordmanager.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mohit.dev.passwordmanager.navigation.Routes

@Composable
fun SplashScreen(
    navController: NavController,
) {

    LaunchedEffect(Unit) {
        navController.navigate(Routes.Lock.route) {
            popUpTo(Routes.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "App Logo",
                tint = Color.White,
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = "Password Manager",
                color = Color.White,
                fontSize = 22.sp
            )
        }
    }
}
