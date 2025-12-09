package mohit.dev.passwordmanager.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddPasswordScreen(navController: NavController) {

    var accountType by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {

        TextField(value = accountType, onValueChange = { accountType = it }, label = { Text("Account Type") })
        TextField(value = username, onValueChange = { username = it }, label = { Text("Username / Email") })
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

        Button(
            onClick = {
                // save encrypted password
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
        ) {
            Text("Save")
        }
    }
}
