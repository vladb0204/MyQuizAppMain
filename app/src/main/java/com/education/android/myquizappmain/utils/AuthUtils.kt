package com.education.android.myquizappmain.utils

import android.util.Patterns

object AuthUtils {
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email)
            .matches() && email.lastIndexOf(".") + 3 <= email.length
    }

    fun isValidPassword(password: String, minLength: Int): Boolean {
        return password.length >= minLength
    }
}