package mohit.dev.passwordmanager.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import mohit.dev.passwordmanager.data.model.PasswordModel
import mohit.dev.passwordmanager.viewmodel.PasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    viewModel: PasswordViewModel = hiltViewModel()
) {
    val passwords by viewModel.passwordList.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var searchQuery by remember { mutableStateOf("") }


    val filteredList = passwords.filter {
        it.accountType.contains(searchQuery, ignoreCase = true) ||
                it.username.contains(searchQuery, ignoreCase = true)
    }

    // Collect UI events
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        },
        topBar = {
            TopAppBar(title = { Text("Password Manager") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {searchQuery = it},
                placeholder = { Text("Search Accounts…") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(20.dp))

            LazyColumn {
                items(filteredList) { password ->
                    PasswordCard(
                        item = password,
                        onDelete = { viewModel.deletePassword(password.id) },
                        onEdit = { onEditClick(password.id) }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun PasswordCard(
    item: PasswordModel,
    onDelete: () -> Unit,
    onEdit: () -> Unit          // <-- NEW PARAM
) {
    var visible by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F1F))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.accountType,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "User: ${item.username}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (visible) item.password else "••••••••",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                Spacer(Modifier.width(10.dp))

                Icon(
                    imageVector = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { visible = !visible },
                    tint = Color.White
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Edit",
                    tint = Color.Cyan,
                    modifier = Modifier
                        .size(25.dp)
                        .clickable { onEdit() }
                )

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .size(25.dp)
                        .clickable { onDelete() }
                )
            }
        }
    }
}
