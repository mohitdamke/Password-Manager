package mohit.dev.passwordmanager.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import mohit.dev.passwordmanager.viewmodel.PasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPasswordScreen(
    onBack: () -> Unit,
    viewModel: PasswordViewModel = hiltViewModel()
) {

    val account by viewModel.accountType.collectAsState()
    val user by viewModel.username.collectAsState()
    val pass by viewModel.password.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Collect UI events (snackbar messages)
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(if (viewModel.accountType.collectAsState().value.isEmpty()) "Add Password" else "Edit Password") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = account,
                onValueChange = viewModel::onAccountTypeChange,
                label = { Text("Account Type") },
                leadingIcon = { Icon(Icons.Default.Security, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = user,
                onValueChange = viewModel::onUsernameChange,
                label = { Text("Username / Email") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = pass,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Password, contentDescription = null) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(22.dp))

            Button(
                onClick = {
                    viewModel.savePassword()
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Password")
            }
        }
    }
}
