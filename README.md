# 🔐 Android Password Manager App

A **secure offline Password Manager** built using Kotlin, Jetpack Compose, Room Database, Hilt, and AES encryption.  
All user passwords are stored safely in **encrypted form**.

---

## 📸 Screenshots

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/5c2814a7-0a4b-40b0-9d56-752f3bbda0fe" width="300"/></td>
    <td><img src="https://github.com/user-attachments/assets/3561d3fe-40f9-45ab-ba05-58b7e1983cc4" width="300"/></td>
  </tr>
  <tr>
    <td align="center"><b>Lock Screen</b></td>
    <td align="center"><b>Home Screen</b></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/0912d1a7-d83d-4f1b-a301-abecca06d6c6" width="300"/></td>
    <td><img src="https://github.com/user-attachments/assets/383d3b94-2604-4496-b808-315d2d5a0ec5" width="300"/></td>
  </tr>
  <tr>
    <td align="center"><b>Edit Password</b></td>
    <td align="center"><b>Add Password</b></td>
  </tr>
</table>

---

## 🎥 Demo Video

<p align="center">
  <video src="https://github.com/user-attachments/assets/807ab423-bb7b-4027-872b-9b9bd523072a" width="420" controls>
</p>

---

## ✨ Features

- 🔒 AES-128 CBC Encryption for secure password storage
- ➕ Add new passwords
- ✏️ Edit existing passwords
- ❌ Delete passwords
- 🔐 4-digit PIN lock screen
- 🗄️ Room database with encrypted fields
- 🧩 MVVM + Repository pattern
- ⚡ StateFlow + Coroutines
- 🧰 Hilt Dependency Injection
- 🎨 Jetpack Compose UI (Dark theme)

---

## 🔑 Encryption

All passwords are encrypted using **AES-128 in CBC mode** with PKCS5 padding.  

**EncryptionUtil.kt**

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionUtil {
    private const val SECRET_KEY = "1234567890123456"   // 16 chars = 128-bit key
    private const val IV = "abcdefghijklmnop"           // 16 chars

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
🗂️ Project Structure
mohit.dev.passwordmanager/
│
├── data/
│   ├── local/          → Room Entities, DAO
│   ├── repository/     → PasswordRepositoryImpl (AES encryption inside)
│   └── security/       → EncryptionUtil
│
├── screen/             → Compose UI screens
├── navigation/         → NavHost + Routes
└── viewmodel/          → ViewModels using StateFlow

🚀 How It Works

User adds or edits a password

Password is encrypted using AES

Encrypted string is saved in Room Database

Password is decrypted when displaying

PIN screen protects access to the app
🛠️ Tech Stack

Kotlin

Jetpack Compose

Room Database

Hilt Dependency Injection

StateFlow + Coroutines

Navigation Compose

AES-128 Encryption

📦 How to Run

Clone repository

Open in Android Studio

Sync Gradle

Run the app

Fully offline — no internet required

👨‍💻 Author

Mohit
Android Developer
