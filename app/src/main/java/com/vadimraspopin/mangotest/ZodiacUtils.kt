package com.vadimraspopin.mangotest

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun getZodiacSign(birthDate: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar = Calendar.getInstance()

    return try {
        val date = sdf.parse(birthDate)

        if (date != null) {
            calendar.time = date

            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            when {
                (month == 3 && day >= 21) || (month == 4 && day <= 19) -> "Овен"
                (month == 4 && day >= 20) || (month == 5 && day <= 20) -> "Телец"
                (month == 5 && day >= 21) || (month == 6 && day <= 20) -> "Близнецы"
                (month == 6 && day >= 21) || (month == 7 && day <= 22) -> "Рак"
                (month == 7 && day >= 23) || (month == 8 && day <= 22) -> "Лев"
                (month == 8 && day >= 23) || (month == 9 && day <= 22) -> "Дева"
                (month == 9 && day >= 23) || (month == 10 && day <= 22) -> "Весы"
                (month == 10 && day >= 23) || (month == 11 && day <= 21) -> "Скорпион"
                (month == 11 && day >= 22) || (month == 12 && day <= 21) -> "Стрелец"
                (month == 12 && day >= 22) || (month == 1 && day <= 19) -> "Козерог"
                (month == 1 && day >= 20) || (month == 2 && day <= 18) -> "Водолей"
                (month == 2 && day >= 19) || (month == 3 && day <= 20) -> "Рыбы"
                else -> "Неизвестно"
            }
        } else {
            "Неизвестно"
        }
    } catch (e: ParseException) {
        e.printStackTrace()
        "Неизвестно"
    }
}