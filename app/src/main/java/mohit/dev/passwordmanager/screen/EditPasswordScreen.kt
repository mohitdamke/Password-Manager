package mohit.dev.passwordmanager.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun EditPasswordScreen(navController: NavController, id: Int) {

    // fetch password by id from DB

    Column(Modifier.padding(16.dp)) {
        Text("Editing Password #$id")

        Button(onClick = { navController.popBackStack() }) {
            Text("Update")
        }

        Button(onClick = { /* delete logic */ }) {
            Text("Delete")
        }
    }
}
