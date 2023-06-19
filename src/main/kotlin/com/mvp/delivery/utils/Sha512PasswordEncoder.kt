package com.mvp.delivery.utils

import org.springframework.security.crypto.password.PasswordEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom


class Sha512PasswordEncoder: PasswordEncoder {
    override fun encode(rawPassword: CharSequence): String {
        return getSecurePassword(rawPassword.toString(), getSecurutyByte())!!
    }

    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean {
        return getSecurePassword(rawPassword.toString(), getSecurutyByte()) == encodedPassword
    }

    private fun getSecurePassword(password: String, salt: ByteArray?): String? {
        var generatedPassword: String? = null
        try {
            val md = MessageDigest.getInstance("SHA-512")
            //md.update(salt)
            val bytes = md.digest(password.toByteArray())
            val sb = StringBuilder()
            for (i in bytes.indices) {
                sb.append(Integer.toString((bytes[i].toInt() and 0xff) + 0x100, 16).substring(1))
            }
            generatedPassword = sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return generatedPassword
    }

    @Throws(NoSuchAlgorithmException::class)
    private fun getSecurutyByte(): ByteArray? {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt
    }
}