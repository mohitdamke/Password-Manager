package mohit.dev.passwordmanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "password_table")
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val accountType: String,
    val username: String,
    val encryptedPassword: String
)
