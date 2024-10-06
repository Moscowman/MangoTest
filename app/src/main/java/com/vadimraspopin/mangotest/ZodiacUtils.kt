package com.vadimraspopin.mangotest

import java.time.LocalDate
import java.time.MonthDay

fun getZodiacSign(birthDate: String): String {
    val date = LocalDate.parse(birthDate)
    val monthDay = MonthDay.from(date)

    return when {
        monthDay >= MonthDay.of(3, 21) && monthDay <= MonthDay.of(4, 19) -> "Овен"
        monthDay >= MonthDay.of(4, 20) && monthDay <= MonthDay.of(5, 20) -> "Телец"
        monthDay >= MonthDay.of(5, 21) && monthDay <= MonthDay.of(6, 20) -> "Близнецы"
        monthDay >= MonthDay.of(6, 21) && monthDay <= MonthDay.of(7, 22) -> "Рак"
        monthDay >= MonthDay.of(7, 23) && monthDay <= MonthDay.of(8, 22) -> "Лев"
        monthDay >= MonthDay.of(8, 23) && monthDay <= MonthDay.of(9, 22) -> "Дева"
        monthDay >= MonthDay.of(9, 23) && monthDay <= MonthDay.of(10, 22) -> "Весы"
        monthDay >= MonthDay.of(10, 23) && monthDay <= MonthDay.of(11, 21) -> "Скорпион"
        monthDay >= MonthDay.of(11, 22) && monthDay <= MonthDay.of(12, 21) -> "Стрелец"
        monthDay >= MonthDay.of(12, 22) || monthDay <= MonthDay.of(1, 19) -> "Козерог"
        monthDay >= MonthDay.of(1, 20) && monthDay <= MonthDay.of(2, 18) -> "Водолей"
        monthDay >= MonthDay.of(2, 19) && monthDay <= MonthDay.of(3, 20) -> "Рыбы"
        else -> "Неизвестно"
    }
}