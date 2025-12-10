package mohit.dev.passwordmanager.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mohit.dev.passwordmanager.data.local.dao.PasswordDao
import mohit.dev.passwordmanager.data.repository.PasswordRepositoryImpl
import mohit.dev.passwordmanager.data.security.EncryptionUtil
import mohit.dev.passwordmanager.domain.PasswordRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePasswordRepository(
        dao: PasswordDao,
        encryptionUtil: EncryptionUtil
    ): PasswordRepository {
        return PasswordRepositoryImpl(dao, encryptionUtil)
    }
}
