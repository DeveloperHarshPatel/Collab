package com.developer_harshpatel.demo.utils

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class Common {

    companion object {
        // check for email id format
        fun isEmailValid(email: String): Boolean {
            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(email)
            return matcher.matches()
        }

        // Password must be alphanumeric and special character and should be more than 6 letter
        fun isPasswordValid(password: String): Boolean {
            val expression = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{6,}$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(password)
            return matcher.matches()
        }

        fun currentTimeStamp(): String {
            val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
            return sdf.format(Date())
        }

        // to save in db
        fun currentTime(): String {
            val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
            return sdf.format(Date())
        }
    }

}