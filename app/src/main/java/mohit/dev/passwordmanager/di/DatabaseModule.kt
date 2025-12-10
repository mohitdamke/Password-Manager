package mohit.dev.passwordmanager.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mohit.dev.passwordmanager.data.local.dao.PasswordDao
import mohit.dev.passwordmanager.data.local.database.PasswordDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PasswordDatabase {
        return Room.databaseBuilder(
                context,
                PasswordDatabase::class.java,
                "password_db"
            ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun providePasswordDao(db: PasswordDatabase): PasswordDao = db.passwordDao()
}
