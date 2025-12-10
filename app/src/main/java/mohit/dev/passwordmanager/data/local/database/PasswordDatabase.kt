package mohit.dev.passwordmanager.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import mohit.dev.passwordmanager.data.local.dao.PasswordDao
import mohit.dev.passwordmanager.data.local.entity.PasswordEntity

@Database(entities = [PasswordEntity::class], version = 1, exportSchema = false)
abstract class PasswordDatabase : RoomDatabase() {
    abstract fun passwordDao(): PasswordDao
}
