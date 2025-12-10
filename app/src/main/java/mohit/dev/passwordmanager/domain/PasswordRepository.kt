package mohit.dev.passwordmanager.domain

import kotlinx.coroutines.flow.Flow
import mohit.dev.passwordmanager.data.model.PasswordModel

interface PasswordRepository {

    fun getAllPasswords(): Flow<List<PasswordModel>>

    suspend fun getPasswordById(id: Int): PasswordModel?

    suspend fun addOrUpdatePassword(model: PasswordModel): Result<Unit>

    suspend fun deletePassword(id: Int): Result<Unit>
}
