package mohit.dev.passwordmanager.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mohit.dev.passwordmanager.data.local.dao.PasswordDao
import mohit.dev.passwordmanager.data.local.entity.PasswordEntity
import mohit.dev.passwordmanager.data.model.PasswordModel
import mohit.dev.passwordmanager.data.security.EncryptionUtil
import mohit.dev.passwordmanager.domain.PasswordRepository
import javax.inject.Inject

class PasswordRepositoryImpl @Inject constructor(
    private val dao: PasswordDao,
    private val encryptionUtil: EncryptionUtil
) : PasswordRepository {

    override fun getAllPasswords(): Flow<List<PasswordModel>> {
        Log.d("PasswordRepo", "Fetching all passwords...")

        return dao.getAllPasswords().map { entities ->

            Log.d("PasswordRepo", "DAO returned ${entities.size} items")

            entities.forEach {
                Log.d("PasswordRepo", "Entity -> id=${it.id}, accountType=${it.accountType}, username=${it.username}")
            }

            entities.map { entity ->
                val model = entity.toModel(encryptionUtil)
                Log.d("PasswordRepo", "Mapped Model -> id=${model.id}, acc=${model.accountType}, user=${model.username}, pass=${model.password}")
                model
            }
        }
    }

    override suspend fun getPasswordById(id: Int): PasswordModel? {
        Log.d("PasswordRepo", "Fetching password by ID: $id")

        val entity = dao.getPasswordById(id)

        if (entity == null) {
            Log.d("PasswordRepo", "getPasswordById: entity NOT FOUND")
            return null
        }

        Log.d("PasswordRepo", "Found entity -> id=${entity.id}, acc=${entity.accountType}, user=${entity.username}")

        val model = entity.toModel(encryptionUtil)

        Log.d("PasswordRepo", "Decrypted model -> pass=${model.password}")

        return model
    }

    override suspend fun addOrUpdatePassword(model: PasswordModel): Result<Unit> {
        return try {

            Log.d("PasswordRepo", "Saving model -> id=${model.id}, acc=${model.accountType}, user=${model.username}, pass=${model.password}")

            val encrypted = encryptionUtil.encrypt(model.password)

            Log.d("PasswordRepo", "Encrypted password: $encrypted")

            val entity = PasswordEntity(
                id = model.id,
                accountType = model.accountType.trim(),
                username = model.username.trim(),
                encryptedPassword = encrypted
            )

            Log.d("PasswordRepo", "Inserting entity into DB: $entity")

            dao.insertPassword(entity)

            Log.d("PasswordRepo", "INSERT SUCCESS")

            Result.success(Unit)

        } catch (e: Exception) {
            Log.e("PasswordRepo", "INSERT ERROR", e)
            Result.failure(e)
        }
    }

    override suspend fun deletePassword(id: Int): Result<Unit> {
        return try {
            Log.d("PasswordRepo", "Deleting password with id=$id")

            val existing = dao.getPasswordById(id)
            if (existing == null) {
                Log.d("PasswordRepo", "Delete failed: ID not found")
                return Result.failure(Exception("Password not found"))
            }

            dao.deletePassword(existing)

            Log.d("PasswordRepo", "DELETE SUCCESS")

            Result.success(Unit)

        } catch (e: Exception) {
            Log.e("PasswordRepo", "DELETE ERROR", e)
            Result.failure(e)
        }
    }
}

private fun PasswordEntity.toModel(encryptionUtil: EncryptionUtil): PasswordModel {

    val decrypted = encryptionUtil.decrypt(encryptedPassword)

    Log.d("PasswordRepo", "Decrypting entity(id=$id) -> decrypted=$decrypted")

    return PasswordModel(
        id = id,
        accountType = accountType,
        username = username,
        password = decrypted
    )
}
