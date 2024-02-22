package com.example.icook.utils

import org.mindrot.jbcrypt.BCrypt

fun hashPassword(password: String): String {
    return BCrypt.hashpw(password, BCrypt.gensalt())
}

fun verifyPassword(inputPassword: String, hashedPassword: String): Boolean {
    return BCrypt.checkpw(inputPassword, hashedPassword)
}