package mohit.dev.passwordmanager.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import mohit.dev.passwordmanager.viewmodel.PasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPasswordScreen(
    navController: NavController,
    passwordId: Int,
    viewModel: PasswordViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val accountType by viewModel.accountType.collectAsState()
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()

    LaunchedEffect(passwordId) {
        Log.d("EditScreen", "Navigated with passwordId=$passwordId")
    }


    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { msg ->
            scope.launch { snackbarHostState.showSnackbar(msg) }
        }
    }

    val passwordList by viewModel.passwordList.collectAsState()

    LaunchedEffect(passwordList) {
        Log.d("EditScreen", "passwordList updated, size=${passwordList.size}")

        val existing = passwordList.firstOrNull { it.id == passwordId }

        Log.d(
            "EditScreen",
            "Matching item: ${existing?.id}, acc=${existing?.accountType}, user=${existing?.username}, pass=${existing?.password}"
        )

        if (existing != null) viewModel.loadPasswordForEdit(existing)
    }



    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Edit Password") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(18.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = accountType,
                onValueChange = { viewModel.onAccountTypeChange(it) },
                label = { Text("Account Type") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = username,
                onValueChange = { viewModel.onUsernameChange(it) },
                label = { Text("Username / Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // ------- Password --------
            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    viewModel.savePassword()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Password")
            }

            Button(
                onClick = {
                    viewModel.deletePassword(passwordId)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete Password", color = MaterialTheme.colorScheme.onError)
            }
        }
    }
}
