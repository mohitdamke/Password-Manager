package mohit.dev.passwordmanager.data.security

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionUtil {

    private const val SECRET_KEY = "1234567890123456"   // 16 chars = 128-bit key
    private const val IV = "abcdefghijklmnop"          // 16 chars

    private fun getCipher(mode: Int): Cipher {
        return Cipher.getInstance("AES/CBC/PKCS5PADDING").apply {
            init(
                mode,
                SecretKeySpec(SECRET_KEY.toByteArray(), "AES"),
                IvParameterSpec(IV.toByteArray())
            )
        }
    }

    fun encrypt(data: String): String {
        val cipher = getCipher(Cipher.ENCRYPT_MODE)
        val encrypted = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    fun decrypt(data: String): String {
        val cipher = getCipher(Cipher.DECRYPT_MODE)
        val decodedData = Base64.decode(data, Base64.DEFAULT)
        return String(cipher.doFinal(decodedData))
    }
}
