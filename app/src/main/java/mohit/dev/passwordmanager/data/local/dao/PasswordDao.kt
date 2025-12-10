package mohit.dev.passwordmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import mohit.dev.passwordmanager.data.local.entity.PasswordEntity

@Dao
interface PasswordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(password: PasswordEntity)

    @Query("SELECT * FROM password_table ORDER BY id DESC")
    fun getAllPasswords(): Flow<List<PasswordEntity>>

    @Query("SELECT * FROM password_table WHERE id = :id")
    suspend fun getPasswordById(id: Int): PasswordEntity?

    @Delete
    suspend fun deletePassword(password: PasswordEntity)
}
