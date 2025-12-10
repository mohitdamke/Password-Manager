package mohit.dev.passwordmanager.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mohit.dev.passwordmanager.data.model.PasswordModel
import mohit.dev.passwordmanager.domain.PasswordRepository
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val repository: PasswordRepository
) : ViewModel() {

    // ---------------- PASSWORD LIST ----------------

    val passwordList: StateFlow<List<PasswordModel>> = repository
        .getAllPasswords()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    // ---------------- FORM FIELDS ----------------

    private val _accountType = MutableStateFlow("")
    val accountType = _accountType.asStateFlow()

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    // Tracks whether user is editing or adding
    private var editId: Int? = null

    // VERY IMPORTANT:
    // Prevents overwriting prefilled values on recomposition
    private var alreadyLoaded = false

    // ---------------- UI EVENT ----------------

    private val _uiEvent = Channel<String>()
    val uiEvent = _uiEvent.receiveAsFlow()

    // ---------------- FIELD UPDATES ----------------

    fun onAccountTypeChange(value: String) { _accountType.value = value }
    fun onUsernameChange(value: String) { _username.value = value }
    fun onPasswordChange(value: String) { _password.value = value }

    // ---------------- LOAD FOR EDIT ----------------

    fun loadPasswordForEdit(password: PasswordModel) {
        Log.d("PasswordVM", "loadPasswordForEdit called with: $password")

        editId = password.id
        _accountType.value = password.accountType
        _username.value = password.username
        _password.value = password.password

        Log.d("PasswordVM", "StateFlow values set: acc=${_accountType.value}, user=${_username.value}, pass=${_password.value}")
    }


    // ---------------- SAVE (ADD OR UPDATE) ----------------

    fun savePassword() {
        val model = PasswordModel(
            id = editId ?: 0, // If null → Add mode
            accountType = _accountType.value.trim(),
            username = _username.value.trim(),
            password = _password.value
        )

        viewModelScope.launch {

            // Validation
            if (model.accountType.isEmpty() || model.username.isEmpty() || model.password.isEmpty()) {
                sendEvent("All fields are required")
                return@launch
            }

            val result = repository.addOrUpdatePassword(model)

            result.fold(
                onSuccess = {
                    if (editId == null) {
                        sendEvent("Password added")
                        clearFields() // only clear during ADD
                    } else {
                        sendEvent("Password updated")
                        // DO NOT CLEAR ON EDIT
                    }
                },
                onFailure = {
                    sendEvent(it.message ?: "Something went wrong")
                }
            )
        }
    }

    // ---------------- DELETE ----------------

    fun deletePassword(id: Int) {
        viewModelScope.launch {
            val result = repository.deletePassword(id)
            result.fold(
                onSuccess = { sendEvent("Password deleted") },
                onFailure = { sendEvent(it.message ?: "Delete failed") }
            )
        }
    }

    // ---------------- UTILS ----------------

    private fun clearFields() {
        editId = null
        alreadyLoaded = false
        _accountType.value = ""
        _username.value = ""
        _password.value = ""
    }

    private fun sendEvent(message: String) {
        viewModelScope.launch {
            _uiEvent.send(message)
        }
    }

    // Call this when leaving Edit screen
    fun resetEditState() {
        editId = null
        alreadyLoaded = false
    }
}
