package com.example.icook.utils

import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.abs

fun getAge(date: LocalDateTime): String {
    val differenceInSeconds = abs(Duration.between(LocalDateTime.now(), date).seconds)
    if (differenceInSeconds <= 60) {
        return "just now"
    }
    if (differenceInSeconds <= 60 * 60) {
        val minutes = differenceInSeconds/60
        return "$minutes minutes ago"
    }
    if (differenceInSeconds <= 60 * 60 * 24) {
        val hours = differenceInSeconds/(60 * 60)
        return "$hours hours ago"
    }
    if (differenceInSeconds <= 60 * 60 * 24 * 30) {
        val days = differenceInSeconds/(60 * 60 * 24)
        return "$days days ago"
    }
    if (differenceInSeconds <= 60 * 60 * 24 * 30 * 12) {
        val months = differenceInSeconds/(60 * 60 * 24 * 30)
        return "$months months ago"
    }
    val years = differenceInSeconds/(60 * 60 * 24 * 30 * 12)
    return "$years years ago"
}