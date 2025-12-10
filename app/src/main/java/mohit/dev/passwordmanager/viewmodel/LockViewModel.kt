package mohit.dev.passwordmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LockViewModel @Inject constructor(): ViewModel() {

    private val _pin = MutableStateFlow("")
    val pin = _pin.asStateFlow()

    private val _error = MutableStateFlow(false)
    val error = _error.asStateFlow()

    // Hardcoded PIN for demo (can fetch from secure storage)
    private val correctPin = "1234"

    fun addDigit(digit: String) {
        if (_pin.value.length < 4) {
            _pin.value += digit
            _error.value = false
        }
    }

    fun removeDigit() {
        if (_pin.value.isNotEmpty()) {
            _pin.value = _pin.value.dropLast(1)
        }
    }

    fun validatePin(input: String): Boolean {
        return if (input == correctPin) {
            _error.value = false
            true
        } else {
            _error.value = true
            false
        }
    }

    fun clearPin() {
        viewModelScope.launch { _pin.value = "" }
    }
}
