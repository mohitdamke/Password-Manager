package mohit.dev.passwordmanager.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mohit.dev.passwordmanager.data.security.EncryptionUtil
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EncryptionModule {

    @Provides
    @Singleton
    fun provideEncryptionUtil(): EncryptionUtil = EncryptionUtil
}
